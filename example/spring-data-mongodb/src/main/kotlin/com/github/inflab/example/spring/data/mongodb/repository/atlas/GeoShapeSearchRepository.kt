package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviews
import com.github.inflab.example.spring.data.mongodb.entity.airbnb.ListingsAndReviewsAddress
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.aggregation.search.GeoShapeRelation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import org.springframework.stereotype.Repository

@Repository
class GeoShapeSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class NameAndAddress(val name: String, val address: ListingsAndReviewsAddress)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoShape/#disjoint-example">Disjoint Example</a>
     */
    fun findByDisjoint(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoShape {
                    relation = GeoShapeRelation.DISJOINT
                    geometry(
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

            limit(3)

            project {
                excludeId()
                +ListingsAndReviews::name
                +ListingsAndReviews::address
                searchScore()
            }
        }

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoShape/#intersects-example">Intersects Example</a>
     */
    fun findByIntersects(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoShape {
                    relation = GeoShapeRelation.INTERSECTS
                    geometry(
                        GeoJsonMultiPolygon(
                            listOf(
                                GeoJsonPolygon(
                                    Point(2.16942, 41.40082),
                                    Point(2.17963, 41.40087),
                                    Point(2.18146, 41.39716),
                                    Point(2.15533, 41.40686),
                                    Point(2.14596, 41.38475),
                                    Point(2.17519, 41.41035),
                                    Point(2.16942, 41.40082),
                                ),
                                GeoJsonPolygon(
                                    Point(2.16365, 41.39416),
                                    Point(2.16963, 41.39726),
                                    Point(2.15395, 41.38005),
                                    Point(2.17935, 41.43038),
                                    Point(2.16365, 41.39416),
                                ),
                            ),
                        ),
                    )
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
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

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoShape/#within-example">Within Example</a>
     */
    fun findByWithin(): AggregationResults<NameAndAddress> {
        val aggregation = aggregation {
            search {
                geoShape {
                    relation = GeoShapeRelation.WITHIN
                    geometry(
                        GeoJsonPolygon(
                            Point(-74.3994140625, 40.5305017757),
                            Point(-74.7290039063, 40.5805846641),
                            Point(-74.7729492188, 40.9467136651),
                            Point(-74.0698242188, 41.1290213475),
                            Point(-73.65234375, 40.9964840144),
                            Point(-72.6416015625, 40.9467136651),
                            Point(-72.3559570313, 40.7971774152),
                            Point(-74.3994140625, 40.5305017757),
                        ),
                    )
                    path(ListingsAndReviews::address..ListingsAndReviewsAddress::location)
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

        return mongoTemplate.aggregate<ListingsAndReviews, NameAndAddress>(aggregation)
    }
}
