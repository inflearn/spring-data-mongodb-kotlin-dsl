package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.annotation.Database
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviews
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviewsAddress
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class NearSearchRepository(
    @Database("sample_mflix") private val mflixMongoTemplate: MongoTemplate,
    @Database("sample_airbnb") private val airbnbMongoTemplate: MongoTemplate,
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
        val name: String,
        val address: ListingsAndReviewsAddress,
        val score: Double,
    )

    data class GeoPropertyTypeDto(
        @Field("property_type")
        val propertyType: String,
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

        return mflixMongoTemplate.aggregate(aggregation, "movies", RuntimeDto::class.java)
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

        return mflixMongoTemplate.aggregate(aggregation, "movies", ReleasedDto::class.java)
    }

    fun findByGeo(): AggregationResults<GeoDto> {
        val aggregation = aggregation {
            search {
                near {
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                    origin(GeoJsonPoint(-8.61308, 41.1413))
                    pivot(1000)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
                searchScore()
            }
        }

        return airbnbMongoTemplate.aggregate(aggregation, "listingsAndReviews", GeoDto::class.java)
    }

    fun findByGeoWithCompound(): AggregationResults<GeoPropertyTypeDto> {
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        text {
                            query("Apartment")
                            path(ListingsAndReviews::propertyType)
                        }
                    }
                    should {
                        near {
                            origin(GeoJsonPoint(114.15027, 22.28158))
                            pivot(1000)
                            path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::propertyType
                +ListingsAndReviews::address
                searchScore()
            }
        }

        return airbnbMongoTemplate.aggregate(aggregation, "listingsAndReviews", GeoPropertyTypeDto::class.java)
    }
}
