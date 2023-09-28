package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonLineString

/**
 * Represents the relation of the query shape geometry to the indexed field geometry.
 *
 * @author Jake Son
 * @since 1.0
 */
enum class GeoShapeRelation {
    /**
     * Indicates that the indexed geometry contains the query geometry.
     */
    CONTAINS,

    /**
     * Indicates that both the query and indexed geometries have nothing in common.
     */
    DISJOINT,

    /**
     * Indicates that both the query and indexed geometries intersect.
     */
    INTERSECTS,

    /**
     * Indicates that the indexed geometry is within the query geometry.
     * You can't use within with [LineString][GeoJsonLineString] or [Point][Point].
     */
    WITHIN,
}
