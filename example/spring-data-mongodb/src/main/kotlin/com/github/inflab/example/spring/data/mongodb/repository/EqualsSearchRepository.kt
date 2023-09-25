package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.sample.Users
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class EqualsSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class NameAndScoreDto(val name: String, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/equals/#boolean-examples">Boolean Example</a>
     */
    fun findVerifiedUser(): AggregationResults<NameAndScoreDto> {
        val aggregation = aggregation {
            search {
                equal {
                    path("verified_user")
                    value(true)
                }
            }

            project {
                +Users::name
                excludeId()
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Users, NameAndScoreDto>(aggregation)
    }

    /**
     * <a href="https://www.mongodb.com/docs/atlas/atlas-search/equals/#date-example">Date Example</a>
     */
    fun findAccountCreated(): AggregationResults<NameAndScoreDto> {
        val aggregation = aggregation {
            search {
                equal {
                    path("account_created")
                    value(LocalDateTime.of(2022, 5, 4, 5, 1, 8))
                }
            }

            project {
                +Users::name
                excludeId()
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Users, NameAndScoreDto>(aggregation)
    }
}
