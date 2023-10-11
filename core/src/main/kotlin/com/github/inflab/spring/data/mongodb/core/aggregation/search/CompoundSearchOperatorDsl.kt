package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure the compound search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#compound">compound</a>
 */
@AggregationMarker
class CompoundSearchOperatorDsl {
    private val document = Document()
    private var hasClause = false

    /**
     * Specify a minimum number of should clauses that must match to include a document in the results.
     * If omitted, it defaults to 0.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#minimumshouldmatch-example">minimumShouldMatch Example</a>
     */
    var minimumShouldMatch: Int? = null

    /**
     * Modify the score of the entire compound clause.
     * You can use score to boost, replace, or otherwise alter the score.
     * If you don't specify score, the returned score is the sum of the scores of all the subqueries in the must and should clauses that generated a match.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring/#std-label-scoring-ref">Score the Documents in the Results</a>
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#scoring-behavior">Scoring Behavior</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    /**
     * Clauses that must match to for a document to be included in the results.
     * The returned score is the sum of the scores of all the subqueries in the clause.
     * Maps to the `AND` boolean operator.
     */
    fun must(mustConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(mustConfiguration).operators
        if (operators.isNotEmpty()) {
            document["must"] = operators
            hasClause = true
        }
    }

    /**
     * Clauses that must not match for a document to be included in the results.
     * `mustNot` clauses don't contribute to a returned document's score.
     * Maps to the `AND NOT` boolean operator.
     */
    fun mustNot(mustNotConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(mustNotConfiguration).operators
        if (operators.isNotEmpty()) {
            document["mustNot"] = operators
            hasClause = true
        }
    }

    /**
     * Clauses that you prefer to match in documents that are included in the results.
     * Documents that contain a match for a should clause have higher scores than documents that don't contain a should clause.
     * The returned score is the sum of the scores of all the subqueries in the clause.
     * Maps to the `OR` boolean operator.
     */
    fun should(shouldConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(shouldConfiguration).operators
        if (operators.isNotEmpty()) {
            document["should"] = operators
            hasClause = true
        }
    }

    /**
     * Clauses that must all match for a document to be included in the results.
     * `filter` clauses do not contribute to a returned document's score.
     */
    fun filter(filterConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(filterConfiguration).operators
        if (operators.isNotEmpty()) {
            document["filter"] = operators
            hasClause = true
        }
    }

    internal fun build(): Document? {
        if (!hasClause) {
            return null
        }

        if (document["should"] != null && minimumShouldMatch != null) {
            document["minimumShouldMatch"] = minimumShouldMatch
        }

        return Document("compound", document)
    }
}
