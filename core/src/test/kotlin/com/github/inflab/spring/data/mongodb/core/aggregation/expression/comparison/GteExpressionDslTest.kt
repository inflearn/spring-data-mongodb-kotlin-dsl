package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class GteExpressionDslTest : FreeSpec({
    fun gte(block: GteExpressionDsl.() -> GteExpressionDsl.Operands) =
        GteExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = gte { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
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
            val expression = gte { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
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
            val expression = gte { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
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
            val result = gte { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gte": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "greaterThanEqual" - {
        "should greaterThanEqual an operand by number" {
            // given
            val number = 100

            // when
            val expression = gte { of(number) greaterThanEqual number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThanEqual an operand by string" {
            // given
            val field = "field"

            // when
            val expression = gte { of(field) greaterThanEqual field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThanEqual an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = gte { of(Test::field) greaterThanEqual Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}gte": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should greaterThanEqual an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = gte { of(expression) greaterThanEqual expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gte": [
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
