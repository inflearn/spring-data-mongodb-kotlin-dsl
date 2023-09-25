package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure range search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/range">range</a>
 */
@AggregationMarker
class RangeSearchOperatorDsl {
    private val document = Document()

    /**
     * Find values greater than (>) the given value.
     *
     * @param value The value to compare to.
     */
    fun gt(value: Number) {
        document["gt"] = value
    }

    /**
     * Find values greater than (>) the given value.
     *
     * @param value The value to compare to.
     */
    fun gt(value: Temporal) {
        document["gt"] = value
    }

    /**
     * Find values greater than or equal to (>=) the given value.
     *
     * @param value The value to compare to.
     */
    fun gte(value: Number) {
        document["gte"] = value
    }

    /**
     * Find values greater than or equal to (>=) the given value.
     *
     * @param value The value to compare to.
     */
    fun gte(value: Temporal) {
        document["gte"] = value
    }

    /**
     * Find values less than (<) the given value.
     *
     * @param value The value to compare to.
     */
    fun lt(value: Number) {
        document["lt"] = value
    }

    /**
     * Find values less than (<) the given value.
     *
     * @param value The value to compare to.
     */
    fun lt(value: Temporal) {
        document["lt"] = value
    }

    /**
     * Find values less than or equal to (<=) the given value.
     *
     * @param value The value to compare to.
     */
    fun lte(value: Number) {
        document["lte"] = value
    }

    /**
     * Find values less than or equal to (<=) the given value.
     *
     * @param value The value to compare to.
     */
    fun lte(value: Temporal) {
        document["lte"] = value
    }

    /**
     * The indexed field or fields to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: String) {
        document["path"] = path.toList().firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun <T : Number> path(vararg path: KProperty<T>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathTemporal")
    fun <T : Temporal> path(vararg path: KProperty<T>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * Modify the score assigned to matching search results.
     * You can modify the default score using the following options:
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

    internal fun build() = Document("range", document)
}
