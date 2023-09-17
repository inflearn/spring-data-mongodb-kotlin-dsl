package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext

/**
 * Encapsulates the aggregation framework `$searchMeta` operation.
 *
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#-searchmeta">$search (aggregation)</a>
 * @author Jake Son
 * @since 1.0
 */
class SearchMetaOperation(
    private val document: Document,
) : AggregationOperation {

    @Deprecated("Deprecated in Java")
    override fun toDocument(context: AggregationOperationContext): Document =
        Document(operator, document)

    override fun getOperator(): String = "\$searchMeta"
}
