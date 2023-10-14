package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators
import org.springframework.data.mongodb.core.aggregation.DateOperators
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class GroupRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("sales")
    data class Sales(val id: Long, val item: String, val price: Double, val quantity: Int, val date: LocalDateTime)

    data class CountDto(val id: Long?, val count: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/group/#count-the-number-of-documents-in-a-collection">Count the Number of Documents in a Collection</a>
     */
    fun count(): AggregationResults<CountDto> {
        val aggregation = aggregation {
            group {
                _idNull()
                "count" accumulator {
                    count()
                }
            }
        }

        return mongoTemplate.aggregate<Sales, CountDto>(aggregation)
    }

    data class GroupByItemHavingDto(val id: String, val totalSaleAmount: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/group/#group-by-item-having">Group by Item Having</a>
     */
    fun groupByItemHaving(): AggregationResults<GroupByItemHavingDto> {
        val aggregation = aggregation {
            group {
                _id(Sales::item)
                "totalSaleAmount" accumulator {
                    // TODO: add $sum operator
                    AccumulatorOperators.Sum.sumOf(
                        ArithmeticOperators.Multiply.valueOf(Sales::price.name).multiplyBy(Sales::quantity.name),
                    )
                }
            }
            // TODO: add $match operator
            match(Criteria.where("totalSaleAmount").gt(100))
        }

        return mongoTemplate.aggregate<Sales, GroupByItemHavingDto>(aggregation)
    }

    data class GroupByDayAndYearDto(
        val id: String,
        val totalSaleAmount: Double,
        val averageQuantity: Double,
        val count: Int,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/group/#group-by-day-and-year">Group by Day and Year</a>
     */
    fun groupByDayAndYear(): AggregationResults<GroupByDayAndYearDto> {
        val aggregation = aggregation {
            // TODO: add $match operator
            match(
                Criteria.where(Sales::date.name).gte(LocalDateTime.of(2014, 1, 1, 0, 0, 0))
                    .lt(LocalDateTime.of(2015, 1, 1, 0, 0, 0)),
            )

            group {
                _id {
                    // TODO: add $dayOfYear operator
                    DateOperators.DateToString.dateOf(Sales::date.name).toString("%Y-%m-%d")
                }
                "totalSaleAmount" accumulator {
                    // TODO: add $sum operator
                    AccumulatorOperators.Sum.sumOf(
                        ArithmeticOperators.Multiply.valueOf(Sales::price.name).multiplyBy(Sales::quantity.name),
                    )
                }
                "averageQuantity" accumulator {
                    // TODO: add $avg operator
                    AccumulatorOperators.Avg.avgOf(Sales::quantity.name)
                }
                "count" accumulator {
                    // TODO: add $sum operator
                    AggregationExpression { org.bson.Document("\$sum", 1) }
                }
            }

            sort {
                "totalSaleAmount" by desc
            }
        }

        return mongoTemplate.aggregate<Sales, GroupByDayAndYearDto>(aggregation)
    }
}
