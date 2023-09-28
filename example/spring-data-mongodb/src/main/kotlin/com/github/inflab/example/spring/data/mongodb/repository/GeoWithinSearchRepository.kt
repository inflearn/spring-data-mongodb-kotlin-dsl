package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviews
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviewsAddress
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.stereotype.Repository

@Repository
class GeoWithinSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class NameAndAddress(val name: String, val address: ListingsAndReviewsAddress)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin/#box-example">Box Example</a>
     */
    fun findByBox(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoWithin {
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                    box(
                        bottomLeft = GeoJsonPoint(112.467, -55.050),
                        topRight = GeoJsonPoint(168.000, -9.133),
                    )
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
            }
        }

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin/#circle-example">Circle Example</a>
     */
    fun findByCircle(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoWithin {
                    circle(
                        center = GeoJsonPoint(-73.54, 45.54),
                        radius = 1600,
                    )
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
            }
        }

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin/#geometry-examples">Polygon Example</a>
     */
    fun findByPolygon(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoWithin {
                    polygon(
                        GeoJsonPolygon(
                            Point(-161.323242, 22.512557),
                            Point(-152.446289, 22.065278),
                            Point(-156.09375, 17.811456),
                            Point(-161.323242, 22.512557),
                        ),
                    )
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
            }
        }

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin/#geometry-examples">MultiPolygon Example</a>
     */
    fun findByMultiPolygon(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoWithin {
                    multiPolygon(
                        GeoJsonMultiPolygon(
                            listOf(
                                GeoJsonPolygon(
                                    Point(-157.8412413882, 21.2882235819),
                                    Point(-157.8607925468, 21.2962046205),
                                    Point(-157.8646640634, 21.3077019651),
                                    Point(-157.862776699, 21.320776283),
                                    Point(-157.8341758705, 21.3133826738),
                                    Point(-157.8349985678, 21.3000822569),
                                    Point(-157.8412413882, 21.2882235819),
                                ),
                                GeoJsonPolygon(
                                    Point(-157.852898124, 21.301208833),
                                    Point(-157.8580050499, 21.3050871833),
                                    Point(-157.8587346108, 21.3098050385),
                                    Point(-157.8508811028, 21.3119240258),
                                    Point(-157.8454308541, 21.30396767),
                                    Point(-157.852898124, 21.301208833),
                                ),
                            ),
                        ),
                    )
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
            }
        }

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }
}
