package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.geo.GeoJson
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure geoWithin operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin">geoWithin</a>
 */
@AggregationMarker
class GeoWithinSearchOperatorDsl {
    private val document = Document()

    /**
     * Object that specifies the bottom left and top right `GeoJSON` points of a box to search within.
     *
     * @param bottomLeft Bottom left `GeoJSON` point.
     * @param topRight Top right `GeoJSON` point.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/geojson">GeoJSON Objects</a>
     */
    fun box(bottomLeft: GeoJsonPoint, topRight: GeoJsonPoint) {
        document["box"] = Document("bottomLeft", bottomLeft.toJson()).append("topRight", topRight.toJson())
    }

    /**
     * Object that specifies the center point and the radius in meters to search within.
     *
     * @param center Center of the circle specified as a GeoJSON point.
     * @param radius Radius, which is a number, specified in meters.
     * Value must be greater than or equal to 0.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/geojson">GeoJSON Objects</a>
     */
    fun circle(center: GeoJsonPoint, radius: Number) {
        document["circle"] = Document("center", center.toJson()).append("radius", radius)
    }

    /**
     * Specifies the `Polygon` to search within.
     * The polygon must be specified as a closed loop where the last position is the same as the first position.
     *
     * @param polygon The Polygon to search within.
     * @see <a href="https://www.mongodb.com/docs/master/reference/geojson/#std-label-geojson-polygon">Polygon</a>
     */
    fun polygon(polygon: GeoJsonPolygon) {
        document["geometry"] = Document("type", "Polygon")
            .append("coordinates", polygon.coordinates.map { it.coordinates.map { point -> listOf(point.x, point.y) } })
    }

    /**
     * Specifies the `MultiPolygon` to search within.
     * The polygon must be specified as a closed loop where the last position is the same as the first position.
     *
     * @param multiPolygon The MultiPolygon to search within.
     * @see <a href="https://www.mongodb.com/docs/master/reference/geojson/#std-label-geojson-polygon">Polygon</a>
     */
    fun multiPolygon(multiPolygon: GeoJsonMultiPolygon) {
        document["geometry"] = Document("type", "MultiPolygon")
            .append(
                "coordinates",
                multiPolygon.coordinates.map {
                    it.coordinates.map { line -> line.coordinates.map { point -> listOf(point.x, point.y) } }
                },
            )
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: String) {
        document["path"] = path.toList().firstOrAll()
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: KProperty<GeoJson<*>>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * See path construction for more information.
     *
     * @param configuration The configuration block for the [PathSearchOptionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(configuration: PathSearchOptionDsl<GeoJson<*>>.() -> Unit) {
        document["path"] = PathSearchOptionDsl<GeoJson<*>>().apply(configuration).build()
    }

    /**
     * Score to assign to matching search results.
     * y default, the score in the results is 1.
     * You can modify the score using the following options:
     *
     * - boost: multiply the result score by the given number.
     * - constant: replace the result score with the given number.
     * - function: replace the result score using the given expression.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring/#std-label-scoring-ref">Score the Documents in the Results</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    private fun GeoJsonPoint.toJson() = Document("type", "Point").append("coordinates", coordinates)

    internal fun build() = Document("geoWithin", document)
}
