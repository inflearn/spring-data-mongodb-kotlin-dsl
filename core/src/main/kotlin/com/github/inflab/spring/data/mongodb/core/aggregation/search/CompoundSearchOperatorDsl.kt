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
        document["must"] = SearchOperatorDsl().apply(mustConfiguration).operators
    }

    /**
     * Clauses that must not match for a document to be included in the results.
     * `mustNot` clauses don't contribute to a returned document's score.
     * Maps to the `AND NOT` boolean operator.
     */
    fun mustNot(mustNotConfiguration: SearchOperatorDsl.() -> Unit) {
        document["mustNot"] = SearchOperatorDsl().apply(mustNotConfiguration).operators
    }

    /**
     * Clauses that you prefer to match in documents that are included in the results.
     * Documents that contain a match for a should clause have higher scores than documents that don't contain a should clause.
     * The returned score is the sum of the scores of all the subqueries in the clause.
     * Maps to the `OR` boolean operator.
     */
    fun should(shouldConfiguration: ShouldSearchClauseDsl.() -> Unit) {
        document["should"] = ShouldSearchClauseDsl().apply(shouldConfiguration).build()
    }

    /**
     * Clauses that must all match for a document to be included in the results.
     * `filter` clauses do not contribute to a returned document's score.
     */
    fun filter(filterConfiguration: SearchOperatorDsl.() -> Unit) {
        document["filter"] = SearchOperatorDsl().apply(filterConfiguration).operators
    }

    internal fun build() = Document("compound", document)
}
