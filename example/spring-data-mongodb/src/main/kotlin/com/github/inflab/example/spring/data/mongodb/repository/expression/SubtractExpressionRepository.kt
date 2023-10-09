package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class SubtractExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("sales")
    data class Sales(
        @Id val id: Long,
        val item: String,
        val price: Long,
        val fee: Long,
        val discount: Long,
        val date: LocalDateTime,
    )

    data class SubtractNumberDto(val id: Long, val item: String, val total: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/subtract/#subtract-numbers">Subtract Numbers</a>
     */
    fun subtractNumber(): AggregationResults<SubtractNumberDto> {
        val aggregation = aggregation {
            project {
                +Sales::item
                "total" expression {
                    subtract {
                        of { add { of(Sales::price) and Sales::fee } } - Sales::discount
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, SubtractNumberDto>(aggregation)
    }

    data class SubtractTwoDatesDto(val id: Long, val item: String, val dateDifference: Long)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/subtract/#subtract-two-dates">Subtract Two Dates</a>
     */
    fun subtractTwoDates(): AggregationResults<SubtractTwoDatesDto> {
        val aggregation = aggregation {
            project {
                +Sales::item
                "dateDifference" expression {
                    subtract {
                        of("\$NOW") - Sales::date
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, SubtractTwoDatesDto>(aggregation)
    }

    data class SubtractMillisecondsDto(val id: Long, val item: String, val dateDifference: LocalDateTime)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/subtract/#subtract-milliseconds-from-a-date">Subtract Milliseconds from a Date</a>
     */
    fun subtractMilliseconds(): AggregationResults<SubtractMillisecondsDto> {
        val aggregation = aggregation {
            project {
                +Sales::item
                "dateDifference" expression {
                    subtract {
                        of(Sales::date) - (5 * 60 * 1000)
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, SubtractMillisecondsDto>(aggregation)
    }
}
