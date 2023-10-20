package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Multiply
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class LetExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("sales")
    data class Sales(@Id val id: Long, val price: Int, val tax: Double, val applyDiscount: Boolean)

    data class FinalTotalDto(val id: Long, val finalTotal: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/let/#example">Example</a>
     */
    fun findFinalTotal(): AggregationResults<FinalTotalDto> {
        val aggregation = aggregation {
            project {
                "finalTotal" expression {
                    let {
                        variable("total") {
                            add { of(Sales::price) and Sales::tax }
                        }
                        variable("discounted") {
                            // TODO: add $cond operator
                            Cond.newBuilder().`when`(Sales::applyDiscount.name).then(0.9).otherwise(1.0)
                        }
                        inExpression {
                            // TODO: add $multiply operator
                            Multiply.valueOf("\$\$total").multiplyBy("\$\$discounted")
                        }
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, FinalTotalDto>(aggregation)
    }
}
