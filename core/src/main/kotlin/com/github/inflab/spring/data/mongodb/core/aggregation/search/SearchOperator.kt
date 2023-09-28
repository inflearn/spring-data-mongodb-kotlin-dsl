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

    /**
     * Supports querying and scoring numeric and date values.
     * This operator can be used to perform a search over:
     *
     * - Number fields of BSON int32, int64, and double data types.
     * - Date fields of BSON date data type in `ISODate` format.
     *
     * You can use the range operator to find results that are within a given numeric or date range.
     *
     * @param configuration The configuration block for the [RangeSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/range">range</a>
     */
    fun range(configuration: RangeSearchOperatorDsl.() -> Unit)

    /**
     * Constrains multiple query predicates to be satisfied from a single element of an array of embedded documents.
     * `embeddedDocument` can be used only for queries over fields of type [How to Index Fields in Arrays of Objects and Documents](https://www.mongodb.com/docs/atlas/atlas-search/field-types/embedded-documents-type/#std-label-bson-data-types-embedded-documents).
     * This operator is similar to [$elemMatch](https://www.mongodb.com/docs/manual/reference/operator/query/elemMatch/)
     *
     * @param configuration The configuration block for the [EmbeddedDocumentSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/embedded-document">embeddedDocument</a>
     */
    fun embeddedDocument(configuration: EmbeddedDocumentSearchOperatorDsl.() -> Unit)

    /**
     * Tests if a path to a specified indexed field name exists in a document.
     * If the specified field exists but is not indexed, the document is not included with the result set.
     * `exists` is often used as part of a compound query in conjunction with other search clauses.
     *
     * @param configuration The configuration block for the [ExistsSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/exists">exists</a>
     */
    fun exists(configuration: ExistsSearchOperatorDsl.() -> Unit)

    /**
     * Supports querying shapes with a relation to a given geometry if indexShapes is set to true in the index definition.
     *
     * @param configuration The configuration block for the [GeoShapeSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoShape">geoShape</a>
     */
    fun geoShape(configuration: GeoShapeSearchOperatorDsl.() -> Unit)

    /**
     * Supports querying geographic points within a given geometry.
     * Only points are returned, even if `indexShapes` value is `true` in the index definition.
     *
     * @param configuration The configuration block for the [GeoWithinSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/geoWithin">geoWithin</a>
     */
    fun geoWithin(configuration: GeoWithinSearchOperatorDsl.() -> Unit)

    /**
     * Returns documents similar to input documents.
     * The moreLikeThis operator allows you to build features for your applications that display similar or alternative results based on one or more given documents.
     *
     * @param configuration The configuration block for the [MoreLikeThisSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/morelikethis">moreLikeThis</a>
     */
    fun moreLikeThis(configuration: MoreLikeThisSearchOperatorDsl.() -> Unit)

    /**
     * Enables queries which use special characters in the search string that can match any character.
     *
     * @param configuration The configuration block for the [WildcardSearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/wildcard">wildcard</a>
     */
    fun wildcard(configuration: WildcardSearchOperatorDsl.() -> Unit)
}
