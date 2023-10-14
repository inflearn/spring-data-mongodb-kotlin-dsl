package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $group stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/group">$group (aggregation)</a>
 */
@AggregationMarker
class GroupStageDsl {
    private var document = Document()
    private val accumulators: MutableMap<String, AggregationExpression> = mutableMapOf()

    fun _idNull() {
        document["_id"] = null
    }

    fun _id(path: String) {
        document["_id"] = "$$path"
    }

    fun _id(property: KProperty<*>) {
        _id(property.toDotPath())
    }

    fun _id(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        document["_id"] = AggregationExpressionDsl().configuration().toDocument()
    }

    infix fun String.accumulator(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        accumulators[this] = AggregationExpressionDsl().configuration()
    }

    internal fun build() = AggregationOperation { context ->
        (document["_id"] as? AggregationExpression)?.let {
            document["_id"] = it.toDocument(context)
        }
        accumulators.forEach { (key, value) ->
            document[key] = value.toDocument(context)
        }

        Document("\$group", document)
    }
}
