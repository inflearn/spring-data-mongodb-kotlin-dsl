package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In
import org.springframework.data.mongodb.core.aggregation.EvaluationOperators.Expr
import org.springframework.data.mongodb.core.aggregation.VariableOperators
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let.ExpressionVariable
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LookupRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class EnrolleeInfo(
        val id: String,
        val name: String,
        val joined: LocalDate,
        val status: String,
    )
    data class ClassDto(
        val id: String,
        val title: String,
        val enrollmentlist: List<String>,
        val days: List<String>,
        @Field("enrollee_info")
        val enrolleeInfo: List<EnrolleeInfo>,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/lookup/#use--lookup-with-an-array">Use $lookup with an Array</a>
     */
    fun joinWithArray(): AggregationResults<ClassDto> {
        val aggregation = aggregation {
            lookup {
                from(MEMBERS)
                localField("enrollmentlist")
                foreignField("name")
                `as`("enrollee_info")
            }
        }

        return mongoTemplate.aggregate(aggregation, CLASSES, ClassDto::class.java)
    }

    data class Match(
        val id: Long,
        val name: String,
        val food: List<String>,
        val beverages: List<String>,
    )
    data class OrderDto(
        val id: Long,
        @Field("restaurant_name")
        val restaurantName: String,
        val drink: String?,
        val matches: List<Match>,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/lookup/#perform-a-concise-correlated-subquery-with--lookup">Perform a Concise Correlated Subquery with $lookup</a>
     */
    fun joinWithSubquery(): AggregationResults<OrderDto> {
        val aggregation = aggregation {
            lookup {
                from(RESTAURANTS)
                localField("restaurant_name")
                foreignField("name")
                let(
                    VariableOperators.Let.just(
                        ExpressionVariable.newVariable("orders_drink").forField("drink"),
                    ),
                )
                pipeline {
                    match(
                        Expr.valueOf(In.arrayOf("\$beverages").containsValue("\$\$orders_drink")),
                    )
                }
                `as`("matches")
            }
        }

        return mongoTemplate.aggregate(aggregation, ORDERS, OrderDto::class.java)
    }

    companion object {
        const val RESTAURANTS = "restaurants"
        const val ORDERS = "orders"
        const val CLASSES = "classes"
        const val MEMBERS = "members"
    }
}
