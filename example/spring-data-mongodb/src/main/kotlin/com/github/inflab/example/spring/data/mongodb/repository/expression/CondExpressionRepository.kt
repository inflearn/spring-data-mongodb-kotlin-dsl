package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class CondExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {
    @Document("inventory")
    data class Inventory(@Id val id: Long, val item: String, val qty: Int)

    data class DiscountDto(val id: Long, val item: String, val discount: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/cond/#example">Example</a>
     */
    fun findDiscount(): AggregationResults<DiscountDto> {
        val aggregation = aggregation {
            project {
                +Inventory::item
                "discount" expression {
                    cond {
                        case {
                            // TODO: add $gte expression
                            ComparisonOperators.Gte.valueOf(Inventory::qty.name).greaterThanEqualToValue(250)
                        } thenValue 30 otherwiseValue 20
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Inventory, DiscountDto>(aggregation)
    }
}
