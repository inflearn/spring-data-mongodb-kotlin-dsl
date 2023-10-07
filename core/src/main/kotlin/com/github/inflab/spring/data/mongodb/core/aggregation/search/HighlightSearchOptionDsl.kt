package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure highlight option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/highlighting/#highlight-search-terms-in-results">Highlight Search Terms in Results</a>
 */
@AggregationMarker
class HighlightSearchOptionDsl {
    private val document = Document()

    /**
     * Maximum number of characters to examine on a document when performing highlighting for a field.
     * If omitted, defaults to 500,000, which means that Atlas Search only examines the first 500,000 characters in the search field in each document for highlighting.
     */
    var maxCharsToExamine: Int? = null
        set(value) {
            document["maxCharsToExamine"] = value
            field = value
        }

    /**
     * Number of high-scoring passages to return per document in the highlights results for each field.
     * A passage is roughly the length of a sentence.
     * If omitted, defaults to 5, which means that for each document, Atlas Search returns the top 5 highest-scoring passages that match the search text.
     *
     */
    var maxNumPassages: Int? = null
        set(value) {
            document["maxNumPassages"] = value
            field = value
        }

    /**
     * Specifies a document field to search. The path field may contain:
     *
     * - A string
     * - An array of strings
     * - A multi analyzer specification
     * - An array containing a combination of strings and multi analyzer specifications
     * - A wildcard character *
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Construct a Query Path</a>
     */
    fun path(configuration: MultiWildcardPathSearchOptionDsl<Any>.() -> Unit) {
        document["path"] = MultiWildcardPathSearchOptionDsl<Any>().apply(configuration).build()
    }

    internal fun get() = document
}
