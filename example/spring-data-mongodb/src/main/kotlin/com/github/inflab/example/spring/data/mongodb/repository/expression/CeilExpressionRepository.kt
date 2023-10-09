package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class CeilExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("samples")
    data class Samples(@Id val id: Long, val value: Double)

    data class CeilDto(val id: Long, val value: Double, val ceilingValue: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ceil/#example">Example</a>
     */
    fun ceilValue(): AggregationResults<CeilDto> {
        val aggregation = aggregation {
            project {
                +Samples::value
                "ceilingValue" expression {
                    ceil(Samples::value)
                }
            }
        }

        return mongoTemplate.aggregate<Samples, CeilDto>(aggregation)
    }
}
