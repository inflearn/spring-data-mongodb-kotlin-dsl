package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.bson.types.ObjectId
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the in operator using idiomatic Kotlin code.
 *
 * @author cranemont
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/in/#in">in</a>
 */
@AggregationMarker
class InSearchOperatorDsl {
    private val document = Document()

    /**
     * The indexed field to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param path Indexed field to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * The indexed field to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param path Indexed field to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(path: KProperty<*>) {
        document["path"] = path.toDotPath()
    }

    /**
     * The score assigned to matching search term results.
     * Use one of the following options to modify the score:
     *
     * - boost: multiply the result score by the given number.
     * - constant: replace the result score with the given number.
     * - function: replace the result score using the given expression.
     *
     *  @param scoreConfiguration The configuration block for [ScoreSearchOptionDsl].
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    fun value(vararg value: Boolean) {
        document["value"] = value.toList()
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    @JvmName("valueBooleanIterable")
    fun value(value: Iterable<Boolean>) {
        document["value"] = value
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    fun value(vararg value: Temporal) {
        document["value"] = value.toList()
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    @JvmName("valueTemporalIterable")
    fun value(value: Iterable<Temporal>) {
        document["value"] = value
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    fun value(vararg value: Number) {
        document["value"] = value.toList()
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    @JvmName("valueNumberIterable")
    fun value(value: Iterable<Number>) {
        document["value"] = value
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    fun value(vararg value: ObjectId) {
        document["value"] = value.toList()
    }

    /**
     * Value or values to search.
     * Value can be either a single value or an array of values of only one of the supported BSON types and can't be a mix of different types.
     *
     * @param value Values to search.
     */
    @JvmName("valueObjectIdIterable")
    fun value(value: Iterable<ObjectId>) {
        document["value"] = value
    }

    internal fun build() = Document("in", document)
}
