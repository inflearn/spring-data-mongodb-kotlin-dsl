package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure score option using idiomatic Kotlin code.
 *
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score">Modify the Score</a>
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class ScoreSearchOptionDsl {
    private val document = Document()

    /**
     * Specifies the number to multiply the default base score by.
     *
     * @param value Value must be a positive number.
     */
    fun boost(value: Double) {
        document["boost"] = Document("value", value)
    }

    /**
     * Specifies the name of the numeric field whose value to multiply the default base score by
     *
     * @param path The name of the numeric field.
     * @param undefined Numeric value to substitute for path if the numeric field specified through path is not found in the documents. If omitted, defaults to 0.
     */
    fun boost(path: String, undefined: Double? = null) {
        document["boost"] = Document("path", path).apply {
            undefined?.let { put("undefined", it) }
        }
    }

    /**
     * Specifies the name of the numeric field whose value to multiply the default base score by
     *
     * @param path [KProperty] of the numeric field.
     * @param undefined Numeric value to substitute for path if the numeric field specified through path is not found in the documents. If omitted, defaults to 0.
     */
    fun boost(path: KProperty<*>, undefined: Double? = null) {
        boost(path.name, undefined)
    }

    internal fun build() = Document("score", document)
}
