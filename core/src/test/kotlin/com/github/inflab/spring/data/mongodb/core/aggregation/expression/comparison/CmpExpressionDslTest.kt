package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class CmpExpressionDslTest : FreeSpec({
    "of" - {
        "should create an operands by field" {
            // given
            val field = "field"

            // when
            val result = CmpExpressionDsl().of(field)

            // then
            result shouldBe CmpExpressionDsl.FirstArg(field)
        }

        "should create an operand by property" {
            // given
            data class Test(val field: String)

            // when
            val result = CmpExpressionDsl().of(Test::field)

            // then
            result shouldBe CmpExpressionDsl.FirstArg("field")
        }

        "should create an operand by expression" {
            // given
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                exp(2)
            }

            // when
            val result = CmpExpressionDsl().of(expression)

            // then
            (result.value as AggregationExpression).shouldBeJson(
                """
                {
                  "${'$'}exp": 2
                }
                """.trimIndent(),
            )
        }
    }

    "compareTo" - {
        "should build an operand by number" {
            // given
            val firstArg = CmpExpressionDsl.FirstArg("firstArg")
            val block: CmpExpressionDsl.() -> AggregationExpression = {
                firstArg compareTo 100
            }

            // when
            val result = CmpExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "$${firstArg.value}",
                    100
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an operand by string" {
            // given
            val firstArg = CmpExpressionDsl.FirstArg("firstArg")
            val secondArg = "secondArg"
            val block: CmpExpressionDsl.() -> AggregationExpression = {
                firstArg compareTo secondArg
            }

            // when
            val result = CmpExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "${'$'}${firstArg.value}",
                    "$secondArg"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an operand by property" {
            // given
            val firstArg = CmpExpressionDsl.FirstArg("firstArg")
            data class Test(val field: Int)
            val block: CmpExpressionDsl.() -> AggregationExpression = {
                firstArg compareByField Test::field
            }

            // when
            val result = CmpExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "${'$'}firstArg",
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an operand by expression" {
            // given
            val firstArg = CmpExpressionDsl.FirstArg("firstArg")
            val expression: AggregationExpressionDsl.() -> AggregationExpression = {
                abs(-100)
            }
            val block: CmpExpressionDsl.() -> AggregationExpression = {
                firstArg compareTo expression
            }

            // when
            val result = CmpExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "${'$'}firstArg",
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
