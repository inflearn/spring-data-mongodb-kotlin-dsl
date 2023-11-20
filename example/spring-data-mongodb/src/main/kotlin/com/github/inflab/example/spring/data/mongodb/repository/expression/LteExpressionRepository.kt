package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class LteExpressionRepository(
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

    data class LteDto(val item: String, val qty: Int, val qtyLte250: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/lte/#example">qty less than or equal 250</a>
     */
    fun qtyLte250(): AggregationResults<LteDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "qtyLte250" expression {
                    lte {
                        of(Inventory::qty) lessThanEqual 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, LteDto>(aggregation)
    }
}
