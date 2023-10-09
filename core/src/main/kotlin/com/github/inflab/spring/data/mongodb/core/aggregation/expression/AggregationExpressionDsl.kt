package com.github.inflab.spring.data.mongodb.core.aggregation.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.AddExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.DivideExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators
import kotlin.reflect.KProperty

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
     * @param property The property of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/abs/#mongodb-expression-exp.-abs">$abs</a>
     */
    fun abs(property: KProperty<Number?>): AggregationExpression =
        ArithmeticOperators.Abs.absoluteValueOf(property.toDotPath())

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
    fun abs(configuration: AggregationExpressionDsl.() -> AggregationExpression): AggregationExpression =
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

    /**
     * Returns the smallest integer greater than or equal to the specified number.
     *
     * @param field The name of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ceil/#mongodb-expression-exp.-ceil">$ceil</a>
     */
    fun ceil(field: String): AggregationExpression = ArithmeticOperators.Ceil.ceilValueOf(field)

    /**
     * Returns the smallest integer greater than or equal to the specified number.
     *
     * @param property The property of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ceil/#mongodb-expression-exp.-ceil">$ceil</a>
     */
    fun ceil(property: KProperty<Number?>): AggregationExpression =
        ArithmeticOperators.Ceil.ceilValueOf(property.toDotPath())

    /**
     * Returns the smallest integer greater than or equal to the specified number.
     *
     * @param value The number.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ceil/#mongodb-expression-exp.-ceil">$ceil</a>
     */
    fun ceil(value: Number): AggregationExpression = ArithmeticOperators.Ceil.ceilValueOf(value)

    /**
     * Returns the smallest integer greater than or equal to the specified number.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ceil/#mongodb-expression-exp.-ceil">$ceil</a>
     */
    fun ceil(configuration: AggregationExpressionDsl.() -> AggregationExpression): AggregationExpression =
        ArithmeticOperators.Ceil.ceilValueOf(AggregationExpressionDsl().configuration())

    /**
     * Returns the result of dividing the first number by the second.
     * Accepts two argument expressions.
     *
     * @param configuration The configuration block for the [DivideExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/divide/#mongodb-expression-exp.-divide">$divide</a>
     */
    fun divide(configuration: DivideExpressionDsl.() -> AggregationExpression): AggregationExpression =
        DivideExpressionDsl().configuration()
}
