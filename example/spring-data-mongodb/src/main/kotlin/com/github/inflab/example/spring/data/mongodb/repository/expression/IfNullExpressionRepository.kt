package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class IfNullExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {
    @Document("inventory")
    data class Inventory(@Id val id: Long, val item: String, val description: String?, val quantity: Int?)

    data class DescriptionDto(val id: Long, val item: String, val description: String)

    /**
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/ifNull">Single Input Expression</a>
     */
    fun findDescription(): AggregationResults<DescriptionDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                "description" expression {
                    ifNull {
                        case(Inventory::description) thenValue "Unspecified"
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, DescriptionDto>(aggregation)
    }

    data class ValueDto(val id: Long, val item: String, val value: Any)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ifNull/#multiple-input-expressions">Multiple Input Expressions</a>
     */
    fun findValue(): AggregationResults<ValueDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                "value" expression {
                    ifNull {
                        case(Inventory::description) or Inventory::quantity thenValue "Unspecified"
                    }
                }
            }
        }

        println(aggregation.toString())

        return mongoTemplate.aggregate<Inventory, ValueDto>(aggregation)
    }
}
