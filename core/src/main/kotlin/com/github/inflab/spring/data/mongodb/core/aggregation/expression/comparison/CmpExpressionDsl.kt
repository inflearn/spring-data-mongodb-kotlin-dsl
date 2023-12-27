package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$cmp` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/cmp">$cmp</a>
 */
@AggregationMarker
class CmpExpressionDsl {

    /**
     * Represents a first argument inside `$cmp`.
     *
     * @property value The first argument.
     */
    @JvmInline
    value class FirstArg(val value: Any)

    /**
     * Creates a new [FirstArg].
     *
     * @param field The name of the field.
     */
    fun of(field: String) = FirstArg(field)

    /**
     * Creates a new [FirstArg].
     *
     * @param property The property of the field.
     */
    fun of(property: KProperty<*>) = of(property.toDotPath())

    /**
     * Creates a new [FirstArg].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun of(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        FirstArg(AggregationExpressionDsl().configuration())

    /**
     * Compares [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    infix fun FirstArg.compareTo(field: Any) = this.toExpression(field)

    /**
     * Compares [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    infix fun FirstArg.compareByField(field: String) = this.toExpression("$$field")

    /**
     * Compares [property] from the previously specified argument.
     *
     * @param property The property of the field.
     */
    infix fun FirstArg.compareByField(property: KProperty<*>) = this.toExpression("$${property.toDotPath()}")

    /**
     * Subtract the result of the given [AggregationExpression] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun FirstArg.compareTo(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        this.toExpression(AggregationExpressionDsl().configuration())

    private fun FirstArg.toExpression(second: Any) = AggregationExpression { context ->
        Document(
            "\$cmp",
            listOf(
                when (value) {
                    is AggregationExpression -> value.toDocument(context)
                    is String -> "$$value"
                    else -> value
                },
                if (second is AggregationExpression) {
                    second.toDocument(context)
                } else {
                    second
                },
            ),
        )
    }
}
