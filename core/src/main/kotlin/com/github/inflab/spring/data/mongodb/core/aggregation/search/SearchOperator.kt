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

    /**
     * checks whether a field matches a value you specify. equals supports querying the following data types:
     * boolean, objectId,  number,  including int32, int64, and double, date
     * You can use the equals operator to query booleans, objectIds, numbers, and dates in arrays. If at least one element in the array matches the "value" field in the equals operator, Atlas Search adds the document to the result set.
     */
    fun equal(configuration: EqualsSearchOperatorDsl.() -> Unit)
}
