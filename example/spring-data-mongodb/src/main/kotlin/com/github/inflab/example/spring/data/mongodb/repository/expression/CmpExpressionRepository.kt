package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class CmpExpressionRepository(
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

    data class CmpDto(val item: String, val qty: Int, val cmpTo250: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/cmp/#example">qty compare to 250</a>
     */
    fun compareTo250(): AggregationResults<CmpDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                +Inventory::qty
                "cmpTo250" expression {
                    cmp {
                        of(Inventory::qty) compareTo 250
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, CmpDto>(aggregation)
    }
}
