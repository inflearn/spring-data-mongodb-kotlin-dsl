package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $addFields stage using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/v7.0/reference/operator/aggregation/addFields">$addFields (aggregation)</a>
 */
@AggregationMarker
class AddFieldsStageDsl {
    private val builder = AddFieldsOperation.builder()

    /**
     * Adds new fields to documents with aggregation expression.
     */
    infix fun String.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        builder.addField(this).withValue(AggregationExpressionDsl().configuration())
    }

    /**
     * Adds new fields to documents from a field.
     */
    infix fun String.set(path: KProperty<Any?>) {
        builder.addField(this).withValue("${'$'}${path.toDotPath()}")
    }

    /**
     * Adds new fields to documents with a value.
     */
    infix fun String.set(value: Any?) {
        builder.addField(this).withValue(value)
    }

    /**
     * Adds new fields to documents from a field.
     */
    infix fun String.setByField(fieldPath: String) {
        builder.addField(this).withValue("$$fieldPath")
    }

    /**
     * Adds new fields to documents with aggregation expression.
     */
    infix fun KProperty<Any?>.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        builder.addField(this.toDotPath()).withValue(AggregationExpressionDsl().configuration())
    }

    /**
     * Adds new fields to documents from a field.
     */
    infix fun KProperty<Any?>.set(path: KProperty<Any?>) {
        builder.addField(this.toDotPath()).withValue("${'$'}${path.toDotPath()}")
    }

    /**
     * Adds new fields to documents with a value.
     */
    infix fun KProperty<Any?>.set(value: Any?) {
        builder.addField(this.toDotPath()).withValue(value)
    }

    /**
     * Adds new fields to documents from a field.
     */
    infix fun KProperty<Any?>.setByField(fieldPath: String) {
        builder.addField(this.toDotPath()).withValue("$$fieldPath")
    }

    internal fun get() = builder.build()
}
