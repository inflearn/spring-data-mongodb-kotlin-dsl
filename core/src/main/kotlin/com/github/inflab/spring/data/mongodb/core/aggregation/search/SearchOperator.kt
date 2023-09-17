package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document

/**
 *  Interface that specifies a MongoDB `$search` operators.
 *
 *  @author Jake Son
 *  @since 1.0
 *  @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#operators">$search operators</a>
 */
interface SearchOperator {
    val operators: MutableList<Document>

    /**
     * Performs a full-text search using the analyzer that you specify in the index configuration.
     * If you omit an analyzer, the text operator uses the default standard analyzer.
     *
     * @param configuration The configuration block for the [TextSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#text">text</a>
     */
    fun text(configuration: TextSearchOperatorDsl.() -> Unit)
}
