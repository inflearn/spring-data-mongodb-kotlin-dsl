package com.github.inflab.spring.data.mongodb.core.aggregation.expression

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression

internal class AggregationExpressionDslTest : FreeSpec({
    fun expression(block: AggregationExpressionDsl.() -> AggregationExpression) =
        AggregationExpressionDsl().block()

    "abs" - {
        "should build an expression by string" {
            // given
            val field = "field"

            // when
            val result = expression { abs(field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}abs": "$$field"
                }
                """.trimIndent(),
            )
        }

        "should build an expression by number" {
            // given
            val number = 100

            // when
            val result = expression { abs(number) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}abs": $number
                }
                """.trimIndent(),
            )
        }

        "should build an expression by expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                abs { abs(field) }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}abs": {
                    "${'$'}abs": "$$field"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "add" - {
        "should build an expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                add { of(field) and field }
            }

            // then
            result.shouldBeJson(
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
    }
})
