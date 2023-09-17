package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure [SearchMetaOperation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#-searchmeta">$searchMeta</a>
 */
@AggregationMarker
class SearchMetaStageDsl : SearchOperator by SearchOperatorDsl(), SearchCollector by SearchCollectorDsl() {
    private val document = Document()

    /**
     * Name of the Atlas Search index to use. If omitted, defaults to `default`.
     * Atlas Search doesn't return results if you misspell the index name or if the specified index doesn't already exist on the cluster.
     */
    var index: String? = null
        set(value) {
            value?.let { document["index"] = it }
            field = value
        }

    /**
     * Configures an option to include a lower bound count of the number of documents that match the query.
     *
     * @param threshold Number of documents to include in the exact count. If omitted, defaults to 1000, which indicates that any number up to 1000 is an exact count and any number above 1000 is a rough count of the number of documents in the result.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/counting/#count-fts-results">Count Atlas Search Results</a>
     */
    fun lowerBoundCount(threshold: Int? = null) {
        document["count"] = Document("type", "lowerBound").apply {
            threshold?.let { put("threshold", it) }
        }
    }

    /**
     * Configures an option to include an exact count of the number of documents that match the query in result set.
     * If the result set is large, Atlas Search might take longer than for `lowerBound` to return the count.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/counting/#count-fts-results">Count Atlas Search Results</a>
     */
    fun totalCount() {
        document["count"] = Document("type", "total")
    }

    internal fun build(): SearchMetaOperation {
        operators.forEach { document.putAll(it) }

        return SearchMetaOperation(document)
    }
}
