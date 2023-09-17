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

    /**
     * Replaces the base score with a specified number.
     *
     * @param value Numeric value to replace the base score with.
     */
    fun constant(value: Double) {
        document["constant"] = Document("value", value)
    }

    /**
     * Configures the following:
     *
     * - Aggregate the scores of multiple matching embedded documents
     * - Modify the score of an [EmbeddedDocument](https://www.mongodb.com/docs/atlas/atlas-search/embedded-document/#std-label-embedded-document-ref) operator after aggregating the scores from matching embedded documents.
     *
     * @param aggregate [ScoreEmbeddedAggregateStrategy] for combining scores of matching embedded documents.
     * @param outerScore [ScoreSearchOptionDsl] for the score modification to apply after applying the aggregation strategy.
     */
    fun embedded(
        aggregate: ScoreEmbeddedAggregateStrategy = ScoreEmbeddedAggregateStrategy.SUM,
        outerScore: (ScoreSearchOptionDsl.() -> Unit)? = null,
    ) {
        document["embedded"] = Document("aggregate", aggregate.name.lowercase()).apply {
            outerScore?.let {
                put("outerScore", ScoreSearchOptionDsl().apply(it).get())
            }
        }
    }

    /**
     *  Configures an option that allows you to alter the final score of the document using a numeric field.
     *  You can specify the numeric field for computing the final score through an [expression](https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#std-label-function-score-expression).
     *  If the final result of the function score is less than 0, Atlas Search replaces the score with 0.
     *
     * @param scoreFunctionConfiguration [ScoreFunctionSearchOptionDsl] to configure the score function.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#function">Score Function</a>
     */
    fun function(scoreFunctionConfiguration: ScoreFunctionSearchOptionDsl.() -> Unit) {
        document["function"] = ScoreFunctionSearchOptionDsl().apply(scoreFunctionConfiguration).build()
    }

    internal fun build() = Document("score", document)

    internal fun get(): Document = document
}
