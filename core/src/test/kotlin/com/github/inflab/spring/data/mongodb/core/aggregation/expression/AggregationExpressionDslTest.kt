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

        "should build an expression by property" {
            // given
            data class Sample(val field: Number?)

            // when
            val result = expression { abs(Sample::field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}abs": "${'$'}field"
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

    "ceil" - {
        "should build an expression by string" {
            // given
            val field = "field"

            // when
            val result = expression { ceil(field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ceil": "$$field"
                }
                """.trimIndent(),
            )
        }

        "should build an expression by property" {
            // given
            data class Sample(val field: Number?)

            // when
            val result = expression { ceil(Sample::field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ceil": "${'$'}field"
                }
                """.trimIndent(),
            )
        }
        "should build an expression by number" {
            // given
            val number = 100

            // when
            val result = expression { ceil(number) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ceil": $number
                }
                """.trimIndent(),
            )
        }

        "should build an expression by expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                ceil { ceil(field) }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ceil": {
                    "${'$'}ceil": "$$field"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "divide" - {
        "should build an expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                divide {
                    of(field) by 123
                }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    "$$field",
                    123
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
