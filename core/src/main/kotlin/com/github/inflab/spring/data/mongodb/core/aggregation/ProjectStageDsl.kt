package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.aggregation.Aggregation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $project stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/project">$project (aggregation)</a>
 */
@AggregationMarker
class ProjectStageDsl {
    private var operation = Aggregation.project()

    /**
     * Specifies the inclusion of a field.
     */
    operator fun String.unaryPlus() {
        operation = operation.andInclude(this)
    }

    /**
     * Specifies the inclusion of a field.
     */
    operator fun KProperty<*>.unaryPlus() {
        operation = operation.andInclude(this.toDotPath())
    }

    /**
     * Specifies the exclusion of a field.
     */
    operator fun String.unaryMinus() {
        operation = operation.andExclude(this)
    }

    /**
     * Specifies the exclusion of a field.
     */
    operator fun KProperty<*>.unaryMinus() {
        operation = operation.andExclude(this.toDotPath())
    }

    /**
     * Specifies the suppression of the _id field.
     */
    fun excludeId() {
        operation = operation.andExclude("_id")
    }

    /**
     * Specifies a projection field with the given [alias].
     *
     * @param alias The alias for the field.
     */
    infix fun String.alias(alias: String) {
        operation = operation.and(this).`as`(alias)
    }

    /**
     * Specifies a projection field with the given [alias].
     *
     * @param alias The alias for the field.
     */
    infix fun KProperty<*>.alias(alias: String) {
        operation = operation.and(this.toDotPath()).`as`(alias)
    }

    /**
     * Specifies an alias that contains the document score of result to from Atlas Search.
     *
     * @param alias The alias for the field.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring">Score the Documents in the Results</a>
     */
    fun searchScore(alias: String = "score") {
        addMetaDataKeyword("searchScore", alias)
    }

    /**
     * Specifies an alias that contains the detail document score of result to from Atlas Search.
     *
     * @param alias The alias for the field.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/get-details">Return the Score Details</a>
     */
    fun searchScoreDetails(alias: String = "scoreDetails") {
        addMetaDataKeyword("searchScoreDetails", alias)
    }

    /**
     * Specifies an alias that contains the highlighted results to from Atlas Search.
     *
     * @param alias The alias for the field.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/highlighting">Highlight Search Terms in Results</a>
     */
    fun searchHighlights(alias: String = "highlights") {
        addMetaDataKeyword("searchHighlights", alias)
    }

    /**
     * Specifies an alias that returns the score associated with the corresponding $text query for each matching document.
     * The text score signifies how well the document matched the search term or terms.
     *
     * @param alias The alias for the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/query/text/#std-label-match-operation-stemmed-words">Search Term or Terms</a>
     */
    fun textScore(alias: String = "score") {
        addMetaDataKeyword("textScore", alias)
    }

    /**
     * Specifies an alias that returns an index key for the document if a non-text index is used.
     * It is for debugging purposes only, and not for application logic, and is preferred over cursor.returnKey().
     */
    fun indexKey(alias: String = "idxKey") {
        addMetaDataKeyword("indexKey", alias)
    }

    private fun addMetaDataKeyword(keyword: String, alias: String) {
        operation = operation.andExpression("{\$meta: \"$keyword\"}").`as`(alias)
    }

    internal fun get() = operation
}
