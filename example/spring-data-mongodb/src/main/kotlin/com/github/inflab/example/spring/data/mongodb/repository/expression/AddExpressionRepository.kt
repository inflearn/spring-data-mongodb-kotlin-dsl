package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class AddExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("sales")
    data class Sales(
        @Id
        val id: Long,
        val item: String,
        val price: Int,
        val fee: Int,
        val date: LocalDateTime,
    )

    data class AddNumberDto(val id: Long, val item: String, val total: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/add/#add-numbers">Add Numbers</a>
     */
    fun addNumbers(): AggregationResults<AddNumberDto> {
        val aggregation = aggregation {
            project {
                +"item"
                "total" expression {
                    add {
                        of(Sales::price) and Sales::fee
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, AddNumberDto>(aggregation)
    }

    data class AddDateDto(val id: Long, val item: String, @Field("billing_date") val billingDate: LocalDateTime)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/add/#perform-addition-on-a-date">Perform Addition on a Date</a>
     */
    fun addDate(): AggregationResults<AddDateDto> {
        val aggregation = aggregation {
            project {
                +"item"
                "billing_date" expression {
                    add {
                        of(Sales::date) and (3.0 * 24 * 60 * 60000)
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Sales, AddDateDto>(aggregation)
    }
}
