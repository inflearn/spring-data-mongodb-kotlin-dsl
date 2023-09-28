package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure text moreLikeThis operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/morelikethis">moreLikeThis</a>
 */
@AggregationMarker
class MoreLikeThisSearchOperatorDsl {
    private val document = Document()

    /**
     * One or more BSON documents that Atlas Search uses to extract representative terms to query for.
     *
     * @param bson One BSON document or an array of documents.
     */
    fun like(vararg bson: Document) {
        document["like"] = bson.toList()
    }

    /**
     * Configures the Score to assign to matching search results.
     * You can modify the default score using the following options:
     *
     * - boost: multiply the result score by the given number.
     * - constant: replace the result score with the given number.
     * - function: replace the result score using the given expression.
     *
     * When you query values in arrays, Atlas Search doesn't alter the score of the matching results based on the number of values inside the array that matched the query.
     * The score would be the same as a single match regardless of the number of matches inside an array.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring/#std-label-scoring-ref">Score the Documents in the Results</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    internal fun build() = Document("moreLikeThis", document)
}
