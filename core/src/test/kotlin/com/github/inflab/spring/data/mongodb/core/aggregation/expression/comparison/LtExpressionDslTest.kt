package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class LtExpressionDslTest : FreeSpec({
    fun lt(block: LtExpressionDsl.() -> LtExpressionDsl.Operands) =
        LtExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = lt { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a operands by field" {
            // given
            val field = "field"

            // when
            val expression = lt { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by property" {
            // given
            data class Test(val field: Long?)

            // when
            val expression = lt { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                exp(2)
            }

            // when
            val result = lt { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "lessThan" - {
        "should lessThan an operand by number" {
            // given
            val number = 100

            // when
            val expression = lt { of(number) lessThan number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThan an operand by string" {
            // given
            val field = "field"

            // when
            val expression = lt { of(field) lessThan field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThan an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = lt { of(Test::field) lessThan Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThan an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = lt { of(expression) lessThan expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    {
                      "${'$'}abs": -100
                    },
                    {
                      "${'$'}abs": -100
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
