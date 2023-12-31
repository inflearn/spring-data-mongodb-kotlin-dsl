package com.github.inflab.spring.data.mongodb.core.aggregation

import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline
import org.springframework.data.mongodb.core.aggregation.VariableOperators

/**
 * Encapsulates the aggregation framework `$lookup` operation.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/lookup/">$lookup (aggregation)</a>
 */
@Suppress("UndocumentedPublicFunction")
class ExtendedLookupOperation : AggregationOperation {
    private val document: Document = Document()
    private var let: VariableOperators.Let? = null
    private var pipeline: AggregationPipeline? = null

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
        this.pipeline = pipeline
    }

    fun let(let: VariableOperators.Let) {
        this.let = let
    }

    fun setAs(value: String) {
        document["as"] = value
    }

    @Deprecated("Deprecated in Java")
    override fun toDocument(context: AggregationOperationContext): Document {
        let?.let {
            document["let"] = it.toDocument(context).get("\$let", Document::class.java)["vars"]
        }
        pipeline?.let {
            document["pipeline"] = it.operations.flatMap { operation -> operation.toPipelineStages(context) }
        }

        return Document(operator, document)
    }

    override fun getOperator(): String = "\$lookup"
}
