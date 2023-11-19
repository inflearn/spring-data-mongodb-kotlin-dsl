package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class GteExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("inventory")
    data class Inventory(
        @Id
        val id: Long,
        val item: String,
        val description: String,
        val qty: Int,
    )

    data class GteDto(val item: String, val qty: Int, val qtyGte250: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/gte/#example">qty greater than or equal 250</a>
     */
    fun qtyGte250(): AggregationResults<GteDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "qtyGte250" expression {
                    gte {
                        of(Inventory::qty) greaterThanEqual 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, GteDto>(aggregation)
    }
}
