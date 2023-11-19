package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class GtExpressionDslTest : FreeSpec({
    fun gt(block: GtExpressionDsl.() -> GtExpressionDsl.Operands) =
        GtExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = gt { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
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
            val expression = gt { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
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
            val expression = gt { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
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
            val result = gt { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gt": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "greaterThan" - {
        "should greaterThan an operand by number" {
            // given
            val number = 100

            // when
            val expression = gt { of(number) greaterThan number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThan an operand by string" {
            // given
            val field = "field"

            // when
            val expression = gt { of(field) greaterThan field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThan an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = gt { of(Test::field) greaterThan Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gt": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThan an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = gt { of(expression) greaterThan expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gt": [
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
