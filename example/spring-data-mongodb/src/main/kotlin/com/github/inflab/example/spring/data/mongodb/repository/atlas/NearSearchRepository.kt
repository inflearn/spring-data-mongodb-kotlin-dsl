package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.annotation.Database
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviews
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviewsAddress
import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
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

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/near/#number-example">Number Example</a>
     */
    fun findByRuntime(): AggregationResults<RuntimeDto> {
        val aggregation = aggregation {
            search {
                index = "runtimes"
                near {
                    path(Movies::runtime)
                    origin(279)
                    pivot(2)
                }
            }

            limit(5)

            project {
                excludeId()
                +Movies::title
                +Movies::runtime
                searchScore()
            }
        }

        return mflixMongoTemplate.aggregate<Movies, RuntimeDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/near/#date-example">Date Example</a>
     */
    fun findByDate(): AggregationResults<ReleasedDto> {
        val aggregation = aggregation {
            search {
                index = "releaseddate"
                near {
                    path(Movies::released)
                    origin(LocalDateTime.of(1915, 9, 13, 0, 0, 0))
                    pivot(7776000000)
                }
            }

            limit(3)

            project {
                excludeId()
                +Movies::title
                +Movies::released
                searchScore()
            }
        }

        return mflixMongoTemplate.aggregate<Movies, ReleasedDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/near/#basic-example">GeoJSON Point Basic Example</a>
     */
    fun findByGeo(): AggregationResults<GeoDto> {
        val aggregation = aggregation {
            search {
                near {
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                    origin(GeoJsonPoint(-8.61308, 41.1413))
                    pivot(1000)
                }
            }

            limit(3)

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
                searchScore()
            }
        }

        return airbnbMongoTemplate.aggregate<ListingsAndReviews, GeoDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/near/#compound-example">GeoJSON Point Compound Example</a>
     */
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

            limit(3)

            project {
                excludeId()
                +ListingsAndReviews::propertyType
                +ListingsAndReviews::address
                searchScore()
            }
        }

        return airbnbMongoTemplate.aggregate<ListingsAndReviews, GeoPropertyTypeDto>(aggregation)
    }
}
