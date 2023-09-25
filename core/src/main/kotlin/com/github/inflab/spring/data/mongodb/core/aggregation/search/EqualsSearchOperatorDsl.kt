package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.bson.types.ObjectId
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure equals search operator using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/equals">equals</a>
 */
@AggregationMarker
class EqualsSearchOperatorDsl {
    private val document = Document()

    /**
     * Indexed field to search.
     *
     * @param path The name of field.
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Indexed field to search.
     *
     * @param path The name of field.
     */
    fun path(path: KProperty<*>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Value to query for boolean field.
     *
     * @param value Value must be a boolean.
     */
    fun value(value: Boolean) {
        document["value"] = value
    }

    /**
     * Value to query for date field.
     *
     * @param value Value must be a date.
     */
    fun value(value: Temporal) {
        document["value"] = value
    }

    /**
     * Value to query for numeric field.
     *
     * @param value Value must be a number.
     */
    fun value(value: Number) {
        document["value"] = value
    }

    /**
     * Value to query for objectId field.
     *
     * @param value Value must be a objectId.
     */
    fun value(value: ObjectId) {
        document["value"] = value
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

    internal fun build() = Document("equals", document)
}
