package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$subtract` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/subtract">$subtract</a>
 */
@AggregationMarker
class SubtractExpressionDsl {
    /**
     * Represents a first argument inside `$subtract`.
     *
     * @property value The first argument.
     */
    @JvmInline
    value class FirstArg(val value: Any)

    /**
     * Creates a new [FirstArg].
     *
     * @param value The number.
     */
    fun of(value: Number) = FirstArg(value)

    /**
     * Creates a new [FirstArg].
     *
     * @param value The date.
     */
    fun of(value: Temporal) = FirstArg(value)

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
    fun of(property: KProperty<Number?>) = of(property.toDotPath())

    /**
     * Creates a new [FirstArg].
     *
     * @param property The property of the field.
     */
    @JvmName("ofTemporal")
    fun of(property: KProperty<Temporal?>) = of(property.toDotPath())

    /**
     * Creates a new [FirstArg].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun of(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        FirstArg(AggregationExpressionDsl().configuration())

    /**
     * Subtract the given [value] from the previously specified argument.
     *
     * @param value The number.
     */
    operator fun FirstArg.minus(value: Number) = this.toExpression(value)

    /**
     * Subtract the given [date] from the previously specified argument.
     *
     * @param date The date.
     */
    operator fun FirstArg.minus(date: Temporal) = this.toExpression(date)

    /**
     * Subtract the value of the given [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    operator fun FirstArg.minus(field: String) = this.toExpression(field)

    /**
     * Subtract the value of the given [property] from the previously specified argument.
     *
     * @param property The property of the field.
     */
    operator fun FirstArg.minus(property: KProperty<Number?>) = this.minus(property.toDotPath())

    /**
     * Subtract the value of the given [property] from the previously specified argument.
     *
     * @param property The property of the field.
     */
    @JvmName("minusTemporal")
    operator fun FirstArg.minus(property: KProperty<Temporal?>) = this.minus(property.toDotPath())

    /**
     * Subtract the result of the given [AggregationExpression] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    operator fun FirstArg.minus(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        this.toExpression(AggregationExpressionDsl().configuration())

    private fun FirstArg.toExpression(second: Any) = AggregationExpression { context ->
        Document(
            "\$subtract",
            listOf(
                when (value) {
                    is AggregationExpression -> value.toDocument(context)
                    is String -> "$$value"
                    else -> value
                },
                when (second) {
                    is AggregationExpression -> second.toDocument(context)
                    is String -> "$$second"
                    else -> second
                },
            ),
        )
    }
}
