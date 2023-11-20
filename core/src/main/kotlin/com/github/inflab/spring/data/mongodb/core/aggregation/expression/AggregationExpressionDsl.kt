package com.github.inflab.spring.data.mongodb.core.aggregation.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.AddExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.DivideExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic.SubtractExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.CmpExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.EqExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.GtExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.GteExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.LtExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.LteExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison.NeExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional.CondExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional.IfNullExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional.SwitchExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.expression.variable.LetExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators
import org.springframework.data.mongodb.core.aggregation.LiteralOperators
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
    fun add(configuration: AddExpressionDsl.() -> AddExpressionDsl.Operands): AggregationExpression =
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

    /**
     * Raises Euler's number (i.e. `e`) to the specified exponent and returns the result.
     *
     * @param field The name of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/exp/#-exp--aggregation-">$exp</a>
     */
    fun exp(field: String): AggregationExpression = ArithmeticOperators.Exp.expValueOf(field)

    /**
     * Raises Euler's number (i.e. `e`) to the specified exponent and returns the result.
     *
     * @param property The property of the field.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/exp/#-exp--aggregation-">$exp</a>
     */
    fun exp(property: KProperty<Number?>): AggregationExpression =
        ArithmeticOperators.Exp.expValueOf(property.toDotPath())

    /**
     * Raises Euler's number (i.e. `e`) to the specified exponent and returns the result.
     *
     * @param value The number.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/exp/#-exp--aggregation-">$exp</a>
     */
    fun exp(value: Number): AggregationExpression = ArithmeticOperators.Exp.expValueOf(value)

    /**
     * Raises Euler's number (i.e. `e`) to the specified exponent and returns the result.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/exp/#-exp--aggregation-">$exp</a>
     */
    fun exp(configuration: AggregationExpressionDsl.() -> AggregationExpression): AggregationExpression =
        ArithmeticOperators.Exp.expValueOf(AggregationExpressionDsl().configuration())

    /**
     * Returns the result of subtracting the second value from the first.
     * If the two values are numbers, return the difference.
     * If the two values are dates, return the difference in milliseconds.
     * If the two values are a date and a number in milliseconds, return the resulting date.
     * Accepts two argument expressions.
     * If the two values are a date and a number, specify the date argument first as it is not meaningful to subtract a date from a number.
     *
     * @param configuration The configuration block for the [SubtractExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/subtract/#mongodb-expression-exp.-subtract">$subtract</a>
     */
    fun subtract(configuration: SubtractExpressionDsl.() -> AggregationExpression): AggregationExpression =
        SubtractExpressionDsl().configuration()

    /**
     * Returns the number of documents in a group.
     */
    fun count() = AggregationExpression {
        Document("\$count", Document())
    }

    /**
     * Returns a sum of numerical values.
     * Ignores non-numeric values.
     */
    fun sum() = AggregationExpression {
        Document("\$sum", 1)
    }

    /**
     * Returns a value without parsing.
     * Use for values that the aggregation pipeline may interpret as an expression.
     *
     * @param T The type of the value.
     * @param value The raw value.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/literal/#mongodb-expression-exp.-literal">$literal</a>
     */
    fun <T> literal(value: T): AggregationExpression = LiteralOperators.valueOf(value as Any).asLiteral()

    /**
     * Returns a value without parsing.
     * Use for values that the aggregation pipeline may interpret as an expression.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/literal/#mongodb-expression-exp.-literal">$literal</a>
     */
    fun literal(configuration: AggregationExpressionDsl.() -> AggregationExpression): AggregationExpression =
        LiteralOperators.valueOf(AggregationExpressionDsl().configuration()).asLiteral()

    /**
     * Defines variables for use within the scope of a subexpression and returns the result of the subexpression.
     * Accepts named parameters.
     * Accepts any number of argument expressions.
     *
     * @param configuration The configuration block for the [LetExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/let/#mongodb-expression-exp.-let">$let</a>
     */
    fun let(configuration: LetExpressionDsl.() -> Unit): AggregationExpression =
        LetExpressionDsl().apply(configuration).build()

    /**
     * Evaluates a boolean expression to return one of the two specified return expressions.
     *
     * @param configuration The configuration block for the [CondExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/cond/#-cond--aggregation-">$cond</a>
     */
    fun cond(configuration: CondExpressionDsl.() -> AggregationExpression): AggregationExpression =
        CondExpressionDsl().configuration()

    /**
     * Evaluates input expressions for null values and returns:
     *
     * - The first non-null input expression value found.
     * - A replacement expression value if all input expressions evaluate to null.
     *
     * `$ifNull` treats undefined values and missing fields as null.
     *
     * @param configuration The configuration block for the [IfNullExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ifNull/#-ifnull--aggregation-">$ifNull</a>
     */
    fun ifNull(configuration: IfNullExpressionDsl.() -> AggregationExpression): AggregationExpression =
        IfNullExpressionDsl().configuration()

    /**
     * Evaluates a series of case expressions.
     * When it finds an expression which evaluates to true, $switch executes a specified expression and breaks out of the control flow.
     *
     * @param configuration The configuration block for the [SwitchExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/switch/#-switch--aggregation-">$switch</a>
     */
    fun switch(configuration: SwitchExpressionDsl.() -> Unit): AggregationExpression =
        SwitchExpressionDsl().apply(configuration).build()

    /**
     * Compares two values and returns:
     * - -1 if the first value is less than the second.
     * - 1 if the first value is greater than the second.
     * - 0 if the two values are equivalent.
     *
     * @param configuration The configuration block for the [CmpExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/cmp/#-cmp--aggregation-">$cmp</a>
     */
    fun cmp(configuration: CmpExpressionDsl.() -> CmpExpressionDsl.Operands): AggregationExpression =
        CmpExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the values are equivalent.
     * - false when the values are not equivalent.
     *
     * @param configuration The configuration block for the [EqExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/eq/#-eq--aggregation-">$eq</a>
     */
    fun eq(configuration: EqExpressionDsl.() -> EqExpressionDsl.Operands): AggregationExpression =
        EqExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the first value is greater than the second value.
     * - false when the first value is less than or equivalent to the second value.
     *
     * @param configuration The configuration block for the [GtExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/gt/#-gt--aggregation-">$gt</a>
     */
    fun gt(configuration: GtExpressionDsl.() -> GtExpressionDsl.Operands): AggregationExpression =
        GtExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the first value is greater than or equivalent to the second value.
     * - false when the first value is less than the second value.
     *
     * @param configuration The configuration block for the [GteExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/gte/#-gte--aggregation-">$gte</a>
     */
    fun gte(configuration: GteExpressionDsl.() -> GteExpressionDsl.Operands): AggregationExpression =
        GteExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the first value is less than the second value.
     * - false when the first value is greater than or equivalent to the second value.
     *
     * @param configuration The configuration block for the [LtExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/lt/#-lt--aggregation-">$lt</a>
     */
    fun lt(configuration: LtExpressionDsl.() -> LtExpressionDsl.Operands): AggregationExpression =
        LtExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the first value is less than or equivalent to the second value.
     * - false when the first value is greater than the second value.
     *
     * @param configuration The configuration block for the [LteExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/lte/#-lte--aggregation-">$lt</a>
     */
    fun lte(configuration: LteExpressionDsl.() -> LteExpressionDsl.Operands): AggregationExpression =
        LteExpressionDsl().build(configuration)

    /**
     * Compares two values and returns:
     * - true when the values are not equivalent.
     * - false when the values are equivalent.
     *
     * @param configuration The configuration block for the [NeExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/ne/#-ne--aggregation-">$lt</a>
     */
    fun ne(configuration: NeExpressionDsl.() -> NeExpressionDsl.Operands): AggregationExpression =
        NeExpressionDsl().build(configuration)
}
