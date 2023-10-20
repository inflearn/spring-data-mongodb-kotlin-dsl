package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators

/**
 * A Kotlin DSL to configure `$switch` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/switch">$switch</a>
 */
@AggregationMarker
class SwitchExpressionDsl {
    private val branches: MutableList<ConditionalOperators.Switch.CaseOperator> = mutableListOf()
    private var defaultValue: Any? = null

    /**
     * Create a new branch.
     *
     * @param caseExpression Can be any valid expression that resolves to a boolean. If the result is not a boolean, it is coerced to a boolean value
     * @param thenValue The result of the case expression if it evaluates to true
     */
    fun branch(caseExpression: AggregationExpressionDsl.() -> AggregationExpression, thenValue: Any) {
        branches.add(
            ConditionalOperators.Switch.CaseOperator
                .`when`(AggregationExpressionDsl().caseExpression())
                .then(thenValue),
        )
    }

    /**
     * @param defaultValue Optional. The path to take if no branch case expression evaluates to true.
     * Although optional, if default is unspecified and no branch case evaluates to true, $switch returns an error.
     */
    fun default(defaultValue: Any) {
        this.defaultValue = defaultValue
    }

    internal fun build(): ConditionalOperators.Switch {
        val aggregation = ConditionalOperators.switchCases(branches)

        return defaultValue?.let { aggregation.defaultTo(it) } ?: aggregation
    }
}
