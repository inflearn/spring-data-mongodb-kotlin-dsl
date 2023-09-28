package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure geoShape operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoShape">geoShape</a>
 */
@AggregationMarker
class GeoShapeSearchOperatorDsl {
    private val document = Document()

    /**
     * Specifies the relation of the query shape geometry to the indexed field geometry.
     */
    var relation: GeoShapeRelation? = null
        set(value) {
            value?.let { document["relation"] = it.name.lowercase() }
            field = value
        }

    /**
     * specifies the `Point` shape to search.
     * The polygon must be specified as a closed loop where the last position is the same as the first position.
     *
     * @see <a href="https://www.mongodb.com/docs/upcoming/reference/geojson/#point">Point</a>
     */
    fun geometry(point: GeoJsonPoint) {
        document["geometry"] = Document("type", "Point").append("coordinates", point.coordinates)
    }

    /**
     * specifies the `Polygon` shape to search.
     * The polygon must be specified as a closed loop where the last position is the same as the first position.
     *
     * @see <a href="https://www.mongodb.com/docs/master/reference/geojson/#std-label-geojson-polygon">Polygon</a>
     */
    fun geometry(polygon: GeoJsonPolygon) {
        document["geometry"] = Document("type", "Polygon").append("coordinates", polygon.coordinates.map { it.coordinates.map { point -> listOf(point.x, point.y) } })
    }

    /**
     * specifies the `MultiPolygon` shape to search.
     * The polygon must be specified as a closed loop where the last position is the same as the first position.
     *
     * @see <a href="https://www.mongodb.com/docs/master/reference/geojson/#std-label-geojson-multipolygon">MultiPolygon</a>
     */
    fun geometry(multiPolygon: GeoJsonMultiPolygon) {
        document["geometry"] = Document("type", "MultiPolygon").append("coordinates", multiPolygon.coordinates.map { it.coordinates.map { line -> line.coordinates.map { point -> listOf(point.x, point.y) } } })
    }

    /**
     * specifies the `LineString` shape to search.
     *
     * @see <a href="https://www.mongodb.com/docs/master/reference/geojson/#std-label-geojson-linestring">LineString</a>
     */
    fun geometry(lintString: GeoJsonLineString) {
        document["geometry"] = Document("type", "LineString").append("coordinates", lintString.coordinates.map { listOf(it.x, it.y) })
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
    @JvmName("pathPoint")
    fun path(vararg path: KProperty<GeoJsonPoint?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathLineString")
    fun path(vararg path: KProperty<GeoJsonLineString?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathPolygon")
    fun path(vararg path: KProperty<GeoJsonPolygon?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathMultiPolygon")
    fun path(vararg path: KProperty<GeoJsonMultiPolygon?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Score to assign to matching search results.
     * By default, the score in the results is 1.
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

    internal fun build() = Document("geoShape", document)
}
