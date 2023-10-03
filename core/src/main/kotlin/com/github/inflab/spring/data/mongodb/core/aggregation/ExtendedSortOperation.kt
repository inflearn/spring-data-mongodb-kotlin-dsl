package com.github.inflab.spring.data.mongodb.core.aggregation

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext

/**
 * Encapsulates the aggregation framework `$sort` operation.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sort">$sort (aggregation)</a>
 */
@Suppress("UndocumentedPublicFunction")
class ExtendedSortOperation : AggregationOperation {
    private val document: Document = Document()

    fun ascending(field: String) {
        document[field] = 1
    }

    fun descending(field: String) {
        document[field] = -1
    }

    fun textScore(field: String) {
        document[field] = Document("\$meta", "textScore")
    }

    @Deprecated("Deprecated in Java")
    override fun toDocument(context: AggregationOperationContext) =
        Document(operator, document)

    override fun getOperator(): String = "\$sort"
}
