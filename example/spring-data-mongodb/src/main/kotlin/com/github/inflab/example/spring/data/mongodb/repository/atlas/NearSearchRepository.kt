package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviewsAddress
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class NearSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class RuntimeDto(
        val title: String,
        val runtime: Int,
        val score: Double,
    )

    data class ReleasedDto(
        val title: String,
        val released: LocalDateTime,
        val score: Double,
    )

    data class GeoDto(
        val title: String,
        val address: ListingsAndReviewsAddress,
        val score: Double,
    )

    fun findByRuntime(): AggregationResults<RuntimeDto> {
        val aggregation = aggregation {
            search {
                index = "runtimes"
                near {
                    path("runtime")
                    origin(279)
                    pivot(2)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +"title"
                +"runtime"
                searchScore()
            }
        }

        return mongoTemplate.aggregate(aggregation, "movies", RuntimeDto::class.java)
    }

    fun findByDate(): AggregationResults<ReleasedDto> {
        val aggregation = aggregation {
            search {
                index = "releaseddate"
                near {
                    path("released")
                    origin(LocalDateTime.of(1915, 9, 13, 0, 0, 0))
                    pivot(7776000000)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +"title"
                +"released"
                searchScore()
            }
        }

        return mongoTemplate.aggregate(aggregation, "movies", ReleasedDto::class.java)
    }
}
