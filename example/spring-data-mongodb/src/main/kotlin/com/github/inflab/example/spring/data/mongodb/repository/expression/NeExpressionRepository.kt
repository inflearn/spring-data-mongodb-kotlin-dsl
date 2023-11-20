package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class NeExpressionRepository(
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

    data class NeDto(val item: String, val qty: Int, val qtyNe250: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ne/#example">not equal to 250</a>
     */
    fun qtyNe250(): AggregationResults<NeDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "qtyNe250" expression {
                    ne {
                        of(Inventory::qty) notEqual 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, NeDto>(aggregation)
    }
}
