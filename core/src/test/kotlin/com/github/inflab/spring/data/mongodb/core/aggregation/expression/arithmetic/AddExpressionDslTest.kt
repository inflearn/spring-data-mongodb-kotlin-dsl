package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import java.time.LocalDateTime

internal class AddExpressionDslTest : FreeSpec({
    fun add(block: AddExpressionDsl.() -> AddExpressionDsl.Operands) =
        AddExpressionDsl().build(block)

    "of" - {
        "should add an operand by string" {
            // given
            val field = "field"

            // when
            val expression = add { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
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
            val expression = add { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = add { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by date" {
            // given
            val date = LocalDateTime.of(2021, 1, 1, 0, 0, 0)

            // when
            val expression = add { of(date) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    {
                      "${'$'}date": "2021-01-01T00:00:00Z"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = add { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    {
                      "${'$'}abs": -100
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "and" - {
        "should add an operand by string" {
            // given
            val field = "field"

            // when
            val expression = add { of(field) and field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = add { of(Test::field) and Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = add { of(number) and number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by date" {
            // given
            val date = LocalDateTime.of(2021, 1, 1, 0, 0, 0)

            // when
            val expression = add { of(date) and date }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}add": [
                    {
                      "${'$'}date": "2021-01-01T00:00:00Z"
                    },
                    {
                      "${'$'}date": "2021-01-01T00:00:00Z"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }

        "should add an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = add { of(expression) and expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}add": [
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
