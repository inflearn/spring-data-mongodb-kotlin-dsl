package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext

class SearchOperation(
    private val document: Document,
) : AggregationOperation {

    @Deprecated("Deprecated in Java")
    override fun toDocument(context: AggregationOperationContext): Document =
        Document(operator, document)

    override fun getOperator(): String = "\$search"
}
