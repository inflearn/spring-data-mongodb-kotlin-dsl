package com.github.inflab.spring.data.mongodb.core.aggregation.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.AddExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators

/**
 * A Kotlin DSL to configure [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation">Aggregation Pipeline Operators</a>
 */
@AggregationMarker
class AggregationExpressionDsl {

    /**
     * Returns the absolute value of a number.
     *
     * @param field The name of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/abs/#mongodb-expression-exp.-abs">$abs</a>
     */
    fun abs(field: String): AggregationExpression = ArithmeticOperators.Abs.absoluteValueOf(field)

    /**
     * Returns the absolute value of a number.
     *
     * @param value The number.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/abs/#mongodb-expression-exp.-abs">$abs</a>
     */
    fun abs(value: Number): AggregationExpression = ArithmeticOperators.Abs.absoluteValueOf(value)

    /**
     * Returns the absolute value of a number.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/abs/#mongodb-expression-exp.-abs">$abs</a>
     */
    fun abs(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        ArithmeticOperators.Abs.absoluteValueOf(AggregationExpressionDsl().configuration())

    /**
     * Adds numbers to return the sum, or adds numbers and a date to return a new date.
     * If adding numbers and a date, treats the numbers as milliseconds.
     * Accepts any number of argument expressions, but at most, one expression can resolve to a date.
     *
     * @param configuration The configuration block for the [AddExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/add/#mongodb-expression-exp.-add">$add</a>
     */
    fun add(configuration: AddExpressionDsl.() -> AddExpressionDsl.Operands) =
        AddExpressionDsl().build(configuration)
}
