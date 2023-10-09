package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class ExpExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("accounts")
    data class Accounts(@Id val id: Long, val interestRate: Double, val presentValue: Long)

    data class ExpDto(val id: Long, val effectiveRate: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/exp/#example">Example</a>
     */
    fun expRate(): AggregationResults<ExpDto> {
        val aggregation = aggregation {
            project {
                "effectiveRate" expression {
                    subtract {
                        of { exp(Accounts::interestRate) } - 1
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Accounts, ExpDto>(aggregation)
    }
}
