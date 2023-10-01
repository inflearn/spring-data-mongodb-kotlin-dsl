package com.github.inflab.spring.data.mongodb.core.aggregation

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline
import org.springframework.data.mongodb.core.aggregation.VariableOperators

class ExtendedLookupOperation : AggregationOperation {
    private val document: Document = Document()

    fun from(from: String?) {
        document["from"] = from
    }

    fun localField(localField: String) {
        document["localField"] = localField
    }

    fun foreignField(foreignField: String) {
        document["foreignField"] = foreignField
    }

    fun pipeline(pipeline: AggregationPipeline) {
        document["pipeline"] = pipeline
    }

    fun let(let: VariableOperators.Let) {
        document["let"] = let
    }

    fun `as`(`as`: String) {
        document["as"] = `as`
    }

    @Deprecated("Deprecated in Java")
    override fun toDocument(context: AggregationOperationContext) =
        Document(operator, document)

    override fun getOperator(): String = "\$lookup"
}
