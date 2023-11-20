package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class LteExpressionDslTest : FreeSpec({
    fun lte(block: LteExpressionDsl.() -> LteExpressionDsl.Operands) =
        LteExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = lte { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
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
            val expression = lte { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
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
            val expression = lte { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
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
            val result = lte { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lte": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "lessThanEqual" - {
        "should lessThanEqual an operand by number" {
            // given
            val number = 100

            // when
            val expression = lte { of(number) lessThanEqual number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThanEqual an operand by string" {
            // given
            val field = "field"

            // when
            val expression = lte { of(field) lessThanEqual field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThanEqual an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = lte { of(Test::field) lessThanEqual Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}lte": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should lessThanEqual an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = lte { of(expression) lessThanEqual expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lte": [
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
