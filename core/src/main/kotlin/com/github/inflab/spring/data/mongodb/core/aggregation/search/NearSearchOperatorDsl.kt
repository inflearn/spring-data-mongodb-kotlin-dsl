package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure near search operator using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/near">near</a>
 */
@AggregationMarker
class NearSearchOperatorDsl {
    private val document = Document()

    /**
     * Indexed field or fields to search.
     * See Path Construction.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: String) {
        document["path"] = path.toList().firstOrAll()
    }

    /**
     * Indexed number type field or fields to search.
     * See Path Construction.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathNumber")
    fun path(vararg path: KProperty<Number?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Indexed date type field or fields to search.
     * See Path Construction.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathDate")
    fun path(vararg path: KProperty<Temporal>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Indexed geo type field or fields to search.
     * See Path Construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathPoint")
    fun path(vararg path: KProperty<GeoJsonPoint>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Origin to query for Number field.
     * This is the origin from which the proximity of the results is measured.
     *
     * For number fields, the value must be of BSON int32, int64, or double data types.
     */
    fun origin(origin: Number) {
        document["origin"] = origin
    }

    /**
     * Origin to query for Date field.
     * This is the origin from which the proximity of the results is measured.
     *
     * For date fields, the value must be an ISODate formatted date.
     * @see <a href="https://www.mongodb.com/docs/upcoming/reference/glossary/#std-term-ISODate">ISODate</a>
     */
    fun origin(origin: Temporal) {
        document["origin"] = origin
    }

    /**
     * Origin to query for Geo field.
     * This is the origin from which the proximity of the results is measured.
     *
     * For geo fields. the value must be a GeoJSON point.
     * @see <a href="https://www.mongodb.com/docs/upcoming/reference/geojson/#std-label-geojson-point">GeoJson Point</a>
     */
    fun origin(origin: GeoJsonPoint) {
        document["origin"] = Document("type", "Point").append("coordinates", origin.coordinates)
    }

    /**
     * Value to use to calculate scores of Atlas Search result documents. Score is calculated using the following formula:
     *               pivot
     * score = ------------------
     *          pivot + distance
     *
     * where distance is the difference between origin and the indexed field value.
     *
     * Results have a score equal to 1/2 (or 0.5) when their indexed field value is pivot units away from origin.
     * The value of pivot must be greater than (i.e. >) 0.
     *
     * If origin is a:
     * - Number, pivot can be specified as an integer or floating point number.
     * - Date, pivot must be specified in milliseconds and can be specified as a 32 or 64 bit integer.
     * - GeoJSON point, pivot is measured in meters and must be specified as an integer or floating point number.
     */
    fun pivot(pivot: Number) {
        document["pivot"] = pivot
    }

    /**
     * The score assigned to matching search term results. Use one of the following options to modify the score:
     *
     * - boost: multiply the result score by the given number.
     * - constant: replace the result score with the given number.
     * - function: replace the result score using the given expression.
     *
     *  @param scoreConfiguration The configuration block for [ScoreSearchOptionDsl]
     *  @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score">Modify the Score</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    internal fun build() = Document("near", document)
}
