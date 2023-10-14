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

    /**
     * Specifies the group key to `null`.
     * If you specify an _id value of null, or any other constant value, the $group stage returns a single document that aggregates values across all of the input documents.
     */
    fun idNull() {
        document["_id"] = null
    }

    /**
     * Specifies the group key to a field path.
     *
     * @param path The field name.
     */
    fun id(path: String) {
        document["_id"] = "$$path"
    }

    /**
     * Specifies the group key to a field path.
     *
     * @param property The property reference.
     */
    fun id(property: KProperty<*>) {
        id(property.toDotPath())
    }

    /**
     * Specifies the group key to an [AggregationExpression].
     *
     * @param configuration The configuration block for the [AggregationExpression].
     */
    fun id(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        document["_id"] = AggregationExpressionDsl().configuration().toDocument()
    }

    /**
     * Computed using the accumulator operators.
     *
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/group/#std-label-accumulators-group">accumulator operators</a>
     */
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
