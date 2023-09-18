package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure the `should` clause of the `compound` search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#std-label-compound-ref">compound</a>
 */
@AggregationMarker
class ShouldSearchClauseDsl : SearchOperator by SearchOperatorDsl() {

    /**
     * Specify a minimum number of should clauses that must match to include a document in the results.
     * If omitted, it defaults to 0.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#minimumshouldmatch-example">minimumShouldMatch Example</a>
     */
    var minimumShouldMatch: Int? = null

    internal fun build() = Document().apply {
        operators.forEach { putAll(it) }
        minimumShouldMatch?.let { put("minimumShouldMatch", it) }
    }
}
