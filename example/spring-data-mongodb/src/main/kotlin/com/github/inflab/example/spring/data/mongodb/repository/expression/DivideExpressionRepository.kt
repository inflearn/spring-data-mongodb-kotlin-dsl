package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class DivideExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("conferencePlanning")
    data class ConferencePlanning(@Id val id: Long, val city: String, val hours: Int, val takes: Int)

    data class DivideDto(val id: Long, val city: String, val workdays: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/divide/#examples">Examples</a>
     */
    fun divide(): AggregationResults<DivideDto> {
        val aggregation = aggregation {
            project {
                +ConferencePlanning::city
                "workdays" expression {
                    divide {
                        of(ConferencePlanning::hours) by 8
                    }
                }
            }
        }

        return mongoTemplate.aggregate<ConferencePlanning, DivideDto>(aggregation)
    }
}
