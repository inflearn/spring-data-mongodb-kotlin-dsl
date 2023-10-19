package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure [SearchOperation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#-search">$search</a>
 */
@AggregationMarker
class SearchStageDsl : SearchOperator by SearchOperatorDsl(), SearchCollector by SearchCollectorDsl() {
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
     * Flag that specifies whether to perform a full document lookup on the backend database or return only stored source fields directly from Atlas Search.
     * If omitted, defaults to false.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/return-stored-source/#std-label-fts-return-stored-source-option">Return Stored Source Fields</a>
     */
    var returnStoredSource: Boolean? = null
        set(value) {
            value?.let { document["returnStoredSource"] = it }
            field = value
        }

    /**
     * Flag that specifies whether to retrieve a detailed breakdown of the score for the documents in the results.
     * If omitted, defaults to false. To view the details, you must use the $meta expression in the $project stage.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/get-details/#std-label-fts-score-details">Return the Score Details</a>
     */
    var scoreDetails: Boolean? = null
        set(value) {
            value?.let { document["scoreDetails"] = it }
            field = value
        }

    /**
     * Document that specifies the tracking option to retrieve analytics information on the search terms.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/tracking">Track Search Terms</a>
     */
    var tracking: String? = null
        set(value) {
            value?.let { document["tracking"] = Document("searchTerms", value) }
            field = value
        }

    /**
     * Configures an option to include a lower bound count of the number of documents that match the query.
     *
     * @param threshold Number of documents to include in the exact count.
     * If omitted, defaults to 1000, which indicates that any number up to 1000 is an exact count and any number above 1000 is a rough count of the number of documents in the result.
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

    /**
     * Document that specifies the highlighting options for displaying search terms in their original context.
     *
     * @param configuration A configuration block to configure [HighlightSearchOptionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/highlighting/#std-label-fts-highlighting">Highlighting</a>
     */
    fun highlight(configuration: HighlightSearchOptionDsl.() -> Unit) {
        document["highlight"] = HighlightSearchOptionDsl().apply(configuration).get()
    }

    /**
     * Document that specifies the fields to sort the Atlas Search results by in ascending or descending order.
     * You can sort by date, number (integer, float, and double values), and string values.
     *
     * @param configuration A configuration block to configure [SortSearchOptionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort">Sort Atlas Search Results</a>
     */
    fun sort(configuration: SortSearchOptionDsl.() -> Unit) {
        SortSearchOptionDsl().apply(configuration).build()?.let { document["sort"] = it }
    }

    internal fun build(): SearchOperation {
        operators.forEach { document.putAll(it) }
        collectors.forEach { document.putAll(it) }

        return SearchOperation(document)
    }
}
