package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class NeExpressionDslTest : FreeSpec({
    fun ne(block: NeExpressionDsl.() -> NeExpressionDsl.Operands) =
        NeExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = ne { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
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
            val expression = ne { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
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
            val expression = ne { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
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
            val result = ne { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ne": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "notEqual" - {
        "should notEqual an operand by number" {
            // given
            val number = 100

            // when
            val expression = ne { of(number) notEqual number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should notEqual an operand by string" {
            // given
            val field = "field"

            // when
            val expression = ne { of(field) notEqual field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should notEqual an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = ne { of(Test::field) notEqual Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}ne": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should notEqual an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = ne { of(expression) notEqual expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ne": [
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
