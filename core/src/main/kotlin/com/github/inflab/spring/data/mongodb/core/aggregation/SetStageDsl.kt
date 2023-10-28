package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.SetOperation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $set stage using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set">$set (aggregation)</a>
 */
@AggregationMarker
class SetStageDsl {
    private val builder = SetOperation.builder()

    private var operation: SetOperation? = null

    /**
     * Adds new fields to documents with aggregation expression.
     *
     * @param configuration The configuration block where you can use DSL to define aggregation expression.
     */
    infix fun String.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        operation = builder.set(this).toValue(AggregationExpressionDsl().configuration())
    }

    /**
     * Adds new fields to documents from a field.
     *
     * @param path The path of the field to contain value to be added.
     */
    infix fun String.set(path: KProperty<Any?>) {
        operation = builder.set(this).toValue("${'$'}${path.toDotPath()}")
    }

    /**
     * Adds new fields to documents with a value.
     *
     * @param value The value of the field to add.
     */
    infix fun String.set(value: Any?) {
        operation = builder.set(this).toValue(value)
    }

    /**
     * Adds new fields to documents from a field.
     *
     * @param fieldPath The path of the field to contain value to be added.
     */
    infix fun String.setByField(fieldPath: String) {
        operation = builder.set(this).toValue("$$fieldPath")
    }

    /**
     * Adds new fields to documents with aggregation expression.
     *
     * @param configuration The configuration block where you can use DSL to define aggregation expression.
     */
    infix fun KProperty<Any?>.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        operation = builder.set(this.toDotPath()).toValue(AggregationExpressionDsl().configuration())
    }

    /**
     * Adds new fields to documents from a field.
     *
     * @param path The path of the field to contain value to be added.
     */
    infix fun KProperty<Any?>.set(path: KProperty<Any?>) {
        operation = builder.set(this.toDotPath()).toValue("${'$'}${path.toDotPath()}")
    }

    /**
     * Adds new fields to documents with a value.
     *
     * @param value The value of the field to add.
     */
    infix fun KProperty<Any?>.set(value: Any?) {
        operation = builder.set(this.toDotPath()).toValue(value)
    }

    /**
     * Adds new fields to documents from a field.
     *
     * @param fieldPath The path of the field to contain value to be added.
     */
    infix fun KProperty<Any?>.setByField(fieldPath: String) {
        operation = builder.set(this.toDotPath()).toValue("$$fieldPath")
    }

    internal fun get() = checkNotNull(operation) { "Set operation must not be null!" }
}
