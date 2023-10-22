package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.BooleanOperators
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class SwitchExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {
    @Document("grades")
    data class Grade(
        @Id val id: Long,
        val name: String,
        val scores: List<Int>,
    )

    data class SummaryDto(
        val id: Long,
        val name: String,
        val summary: String,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/switch/#example">Example</a>
     */
    fun findSummary(): AggregationResults<SummaryDto> {
        val aggregation = aggregation {
            project {
                +Grade::name
                "summary" expression {
                    switch {
                        branch(
                            // TODO: add $gte and $avg operator
                            {
                                ComparisonOperators.Gte.valueOf(AccumulatorOperators.Avg.avgOf(Grade::scores.name))
                                    .greaterThanEqualToValue(90)
                            },
                            "Doing great!",
                        )
                        branch(
                            // TODO: add $and, $gte, $avg and $lt operator
                            {
                                BooleanOperators.And.and(
                                    ComparisonOperators.Gte.valueOf(AccumulatorOperators.Avg.avgOf(Grade::scores.name))
                                        .greaterThanEqualToValue(80),
                                    ComparisonOperators.Lt.valueOf(AccumulatorOperators.Avg.avgOf(Grade::scores.name))
                                        .lessThanValue(90),
                                )
                            },
                            "Doing pretty well.",
                        )
                        branch(
                            // TODO: add $lt and $avg operator
                            {
                                ComparisonOperators.Lt.valueOf(AccumulatorOperators.Avg.avgOf(Grade::scores.name))
                                    .lessThanValue(80)
                            },
                            "Needs improvement.",
                        )
                        default("No scores found.")
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Grade, SummaryDto>(aggregation)
    }
}
