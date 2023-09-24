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
     * Checks whether a field matches a value you specify. equals supports querying the following data types:
     * boolean, objectId,  number,  including int32, int64, and double, date
     * You can use the equals operator to query booleans, objectIds, numbers, and dates in arrays. If at least one element in the array matches the "value" field in the equals operator, Atlas Search adds the document to the result set.
     *
     * @param configuration The configuration block for the [EqualsSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/equals/#equals">equals</a>
     */
    fun equal(configuration: EqualsSearchOperatorDsl.() -> Unit)

    /**
     * Combines two or more operators into a single query.
     * Each element of a compound query is called a clause, and each clause consists of one or more sub-queries.
     *
     * @param configuration The configuration block for the [CompoundSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/compound/#compound">compound</a>
     */
    fun compound(configuration: CompoundSearchOperatorDsl.() -> Unit)

    /**
     * Performs search for documents containing an ordered sequence of terms using the `analyzer` specified in the `index configuration`.
     * If no analyzer is specified, the default `standard` analyzer is used.
     *
     * @param configuration The configuration block for the [PhraseSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/phrase">phrase</a>
     */
    fun phrase(configuration: PhraseSearchOperatorDsl.() -> Unit)
}
