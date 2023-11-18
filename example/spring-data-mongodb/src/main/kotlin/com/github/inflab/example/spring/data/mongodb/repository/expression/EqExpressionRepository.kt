package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class EqExpressionRepository(
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

    data class EqDto(val item: String, val qty: Int, val cmpTo250: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/eq/#example">Equal qty to 250</a>
     */
    fun qtyEq250(): AggregationResults<EqDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "qtyEq250" expression {
                    eq {
                        of(Inventory::qty) equal 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, EqDto>(aggregation)
    }
}
