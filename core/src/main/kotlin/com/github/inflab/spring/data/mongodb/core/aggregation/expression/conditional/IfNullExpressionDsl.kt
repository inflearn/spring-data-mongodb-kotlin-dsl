package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.IfNull.ThenBuilder
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$ifNull` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/ifNull">$ifNull</a>
 */
@AggregationMarker
class IfNullExpressionDsl {
    /**
     * Represent a container for [ThenBuilder].
     *
     * @property value The [ThenBuilder].
     */
    @JvmInline
    value class Case(val value: ThenBuilder)

    /**
     * Create a new [Case].
     *
     * @param field The name of the field.
     */
    fun case(field: String) = Case(ConditionalOperators.ifNull(field))

    /**
     * Create a new [Case].
     *
     * @param property The property of the field.
     */
    fun case(property: KProperty<*>) = case(property.toDotPath())

    /**
     * Create a new [Case].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun case(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Case(ConditionalOperators.ifNull(AggregationExpressionDsl().configuration()))

    /**
     * Or the given [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    infix fun Case.or(field: String) = Case(this.value.orIfNull(field))

    /**
     * Or the given [property] from the previously specified argument.
     *
     * @param property The property of the field.
     */
    infix fun Case.or(property: KProperty<*>) = or(property.toDotPath())

    /**
     * Or the given [configuration] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Case.or(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Case(this.value.orIfNull(AggregationExpressionDsl().configuration()))

    /**
     * Otherwise the given [value] from the previously specified argument.
     *
     * @param value the value to be used if the `$ifNull` condition evaluates `true`.
     * Can be a [Document], a value that is supported by MongoDB or a value that can be converted to a MongoDB representation but must not be `null`.
     */
    infix fun Case.thenValue(value: Any) = this.value.then(value)

    /**
     * Otherwise the given [field] from the previously specified argument.
     *
     * @param field The field holding the replacement value.
     */
    infix fun Case.then(field: String) = this.value.thenValueOf(field)

    /**
     * Otherwise the given [property] from the previously specified argument.
     *
     * @param property The property of the field.
     */
    infix fun Case.then(property: KProperty<*>) = then(property.toDotPath())

    /**
     * Otherwise the given [configuration] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Case.then(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        this.value.thenValueOf(AggregationExpressionDsl().configuration())
}
