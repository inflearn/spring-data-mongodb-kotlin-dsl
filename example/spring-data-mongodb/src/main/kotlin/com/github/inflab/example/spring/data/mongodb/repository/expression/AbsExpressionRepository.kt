package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class AbsExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("temperatureChange")
    data class TemperatureChange(@Id val id: Long, val startTemp: Int, val endTemp: Int)

    data class AbsDto(val id: Long, val delta: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/abs/#example">Example</a>
     */
    fun absDelta(): AggregationResults<AbsDto> {
        val aggregation = aggregation {
            project {
                "delta" expression {
                    abs {
                        ArithmeticOperators.Subtract.valueOf("startTemp").subtract("endTemp")
                    }
                }
            }
        }

        return mongoTemplate.aggregate<TemperatureChange, AbsDto>(aggregation)
    }
}
