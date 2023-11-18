package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class EqExpressionDslTest : FreeSpec({
    fun eq(block: EqExpressionDsl.() -> EqExpressionDsl.Operands) =
        EqExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = eq { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
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
            val expression = eq { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
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
            val expression = eq { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
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
            val result = eq { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}eq": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "equal" - {
        "should equal an operand by number" {
            // given
            val number = 100

            // when
            val expression = eq { of(number) equal number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should equal an operand by string" {
            // given
            val field = "field"

            // when
            val expression = eq { of(field) equal field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should equal an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = eq { of(Test::field) equal Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}eq": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should equal an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = eq { of(expression) equal expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}eq": [
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
