package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure [SearchOperation]  using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/search">$search (aggregation)</a>
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/">Return Atlas Search Results or Metadata</a>
 */
@AggregationMarker
class SearchStageDsl {
    private val document = Document()

    /**
     * Name of the Atlas Search index to use. If omitted, defaults to `default`.
     * Atlas Search doesn't returns results if you misspell the index name or if the specified index doesn't already exist on the cluster.
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

    internal fun build() = SearchOperation(document)
}
