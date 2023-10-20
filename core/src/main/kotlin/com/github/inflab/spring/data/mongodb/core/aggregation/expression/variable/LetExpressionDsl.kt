package com.github.inflab.spring.data.mongodb.core.aggregation.expression.variable

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let.ExpressionVariable

/**
 * A Kotlin DSL to configure `$let` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/let">$let</a>
 */
@AggregationMarker
class LetExpressionDsl {
    private val vars: MutableList<ExpressionVariable> = mutableListOf()
    private var inExpression: AggregationExpression? = null

    /**
     * Adds a new variable to the list of variables.
     *
     * @param name The name of the variable.
     */
    fun variable(name: String) {
        vars.add(ExpressionVariable.newVariable(name))
    }

    /**
     * Adds a new variable to the list of variables.
     *
     * @param variable The [ExpressionVariable] to add.
     */
    fun variable(variable: ExpressionVariable) {
        vars.add(variable)
    }

    /**
     * Adds a new variable to the list of variables.
     *
     * @param name The name of the variable.
     * @param expressionObject The [Document] that represents the expression.
     */
    fun variable(name: String, expressionObject: Document) {
        vars.add(ExpressionVariable.newVariable(name).forExpression(expressionObject))
    }

    /**
     * Adds a new variable to the list of variables.
     *
     * @param name The name of the variable.
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun variable(name: String, configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        vars.add(ExpressionVariable.newVariable(name).forExpression(AggregationExpressionDsl().configuration()))
    }

    /**
     * Configures the `in` option.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun inExpression(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        inExpression = AggregationExpressionDsl().configuration()
    }

    internal fun build(): Let {
        val result = inExpression ?: AggregationExpression { Document() }

        return Let.define(vars).andApply(result)
    }
}
