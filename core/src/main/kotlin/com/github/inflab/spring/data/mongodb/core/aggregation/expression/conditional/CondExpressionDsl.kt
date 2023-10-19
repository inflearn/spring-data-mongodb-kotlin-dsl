package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond.OtherwiseBuilder
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond.ThenBuilder
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$cond` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/cond">$cond</a>
 */
@AggregationMarker
class CondExpressionDsl {
    /**
     * Represent a container for [ThenBuilder].
     *
     * @property value The [ThenBuilder].
     */
    @JvmInline
    value class Case(val value: ThenBuilder)

    /**
     * Represent a container for [OtherwiseBuilder].
     *
     * @property value The [OtherwiseBuilder].
     */
    data class Then(val value: OtherwiseBuilder)

    /**
     * Create a new [Case].
     *
     * @param booleanExpression The boolean expression.
     */
    fun case(booleanExpression: Document) =
        Case(ConditionalOperators.Cond.newBuilder().`when`(booleanExpression))

    /**
     * Create a new [Case].
     *
     * @param booleanField The name of the field.
     */
    fun case(booleanField: String) =
        Case(ConditionalOperators.Cond.newBuilder().`when`(booleanField))

    /**
     * Create a new [Case].
     *
     * @param property The property of the field.
     */
    fun case(property: KProperty<Boolean?>) =
        Case(ConditionalOperators.Cond.newBuilder().`when`(property.toDotPath()))

    /**
     * Create a new [Case].
     *
     * @param criteria The criteria.
     */
    fun case(criteria: CriteriaDefinition) =
        Case(ConditionalOperators.Cond.newBuilder().`when`(criteria))

    /**
     * Create a new [Case].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun case(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Case(ConditionalOperators.Cond.newBuilder().`when`(AggregationExpressionDsl().configuration()))

    /**
     * Then the given [value] from the previously specified argument.
     *
     * @param value the value to be used if the condition evaluates `true`.
     * Can be a [Document], a value that is supported by MongoDB or a value that can be converted to a MongoDB representation but must not be `null`.
     */
    infix fun Case.then(value: Any) = Then(this.value.then(value))

    /**
     * Then the given [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    infix fun Case.then(field: String) = Then(this.value.thenValueOf(field))

    /**
     * Then the given [configuration] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Case.then(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Then(this.value.thenValueOf(AggregationExpressionDsl().configuration()))

    /**
     * Otherwise the given [value] from the previously specified argument.
     *
     * @param value the value to be used if the condition evaluates `false`.
     * Can be a [Document], a value that is supported by MongoDB or a value that can be converted to a MongoDB representation but must not be `null`.
     */
    infix fun Then.otherwise(value: Any) = this.value.otherwise(value)

    /**
     * Otherwise the given [field] from the previously specified argument.
     *
     * @param field The name of the field.
     */
    infix fun Then.otherwise(field: String) = this.value.otherwiseValueOf(field)

    /**
     * Otherwise the given [configuration] from the previously specified argument.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Then.otherwise(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        this.value.otherwiseValueOf(AggregationExpressionDsl().configuration())
}
