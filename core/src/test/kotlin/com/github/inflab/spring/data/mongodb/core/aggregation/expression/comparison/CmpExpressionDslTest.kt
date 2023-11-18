package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class CmpExpressionDslTest : FreeSpec({
    fun cmp(block: CmpExpressionDsl.() -> CmpExpressionDsl.Operands) =
        CmpExpressionDsl().build(block)

    "of" - {
        "should add an operand by number" {
            // given
            val number = 100

            // when
            val expression = cmp { of(number) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
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
            val expression = cmp { of(field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
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
            val expression = cmp { of(Test::field) }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
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
            val result = cmp { of(expression) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    {
                      "${'$'}exp": 2
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "compareTo" - {
        "should compareTo an operand by number" {
            // given
            val number = 100

            // when
            val expression = cmp { of(number) compareTo number }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    100,
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should compareTo an operand by string" {
            // given
            val field = "field"

            // when
            val expression = cmp { of(field) compareTo field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should compareTo an operand by property" {
            // given
            data class Test(val field: Int?)

            // when
            val expression = cmp { of(Test::field) compareTo Test::field }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "${'$'}field",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should compareTo an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }

            // when
            val result = cmp { of(expression) compareTo expression }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
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
