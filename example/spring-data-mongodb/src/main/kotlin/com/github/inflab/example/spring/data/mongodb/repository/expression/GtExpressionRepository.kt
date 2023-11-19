package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class GtExpressionRepository(
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

    data class GtDto(val item: String, val qty: Int, val qtyGt250: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/gt/#example">qty greater than 250</a>
     */
    fun qtyGt250(): AggregationResults<GtDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "qtyGt250" expression {
                    gt {
                        of(Inventory::qty) greaterThan 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, GtDto>(aggregation)
    }
}
