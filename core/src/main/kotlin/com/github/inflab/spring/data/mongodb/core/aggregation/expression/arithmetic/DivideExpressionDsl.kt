package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$divide` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/divide">$divide</a>
 */
@AggregationMarker
class DivideExpressionDsl {
    @JvmInline
    value class Dividend(val value: Divide)

    /**
     * Creates a new [Dividend].
     *
     * @param value The number.
     */
    fun of(value: Number) = Dividend(Divide.valueOf(value))

    /**
     * Creates a new [Dividend].
     *
     * @param field The name of the field.
     */
    fun of(field: String) = Dividend(Divide.valueOf(field))

    /**
     * Creates a new [Dividend].
     *
     * @param property The property of the field.
     */
    fun of(property: KProperty<*>) = of(property.toDotPath())

    /**
     * Creates a new [Dividend].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun of(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Dividend(Divide.valueOf(AggregationExpressionDsl().configuration()))

    /**
     * Divide by the given [value].
     *
     * @param value The number.
     */
    infix fun Dividend.by(value: Number): Divide = this.value.divideBy(value)

    /**
     * Divide by the value of the given [field].
     *
     * @param field The name of the field.
     */
    infix fun Dividend.by(field: String): Divide = this.value.divideBy(field)

    /**
     * Divide by the value of the given [property].
     *
     * @param property The property of the field.
     */
    infix fun Dividend.by(property: KProperty<*>): Divide = this.value.divideBy(property.toDotPath())

    /**
     * Divide by the result of the given [AggregationExpression].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Dividend.by(configuration: AggregationExpressionDsl.() -> AggregationExpression): Divide =
        this.value.divideBy(AggregationExpressionDsl().configuration())
}
