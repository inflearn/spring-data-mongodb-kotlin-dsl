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

    "exp" - {
        "should build an expression by string" {
            // given
            val field = "field"

            // when
            val result = expression { exp(field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}exp": "$$field"
                }
                """.trimIndent(),
            )
        }

        "should build an expression by property" {
            // given
            data class Sample(val field: Number?)

            // when
            val result = expression { exp(Sample::field) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}exp": "${'$'}field"
                }
                """.trimIndent(),
            )
        }
        "should build an expression by number" {
            // given
            val number = 100

            // when
            val result = expression { exp(number) }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}exp": $number
                }
                """.trimIndent(),
            )
        }

        "should build an expression by expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                exp { exp(field) }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}exp": {
                    "${'$'}exp": "$$field"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "subtract" - {
        "should build an expression" {
            // given
            val field = "field"

            // when
            val result = expression {
                subtract {
                    of(field) - 123
                }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    "$$field",
                    123
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "count" - {
        "should build an expression" {
            // when
            val result = expression { count() }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}count": {}
                }
                """.trimIndent(),
            )
        }
    }

    "sum" - {
        "should build an expression" {
            // when
            val result = expression { sum() }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sum": 1
                }
                """.trimIndent(),
            )
        }
    }

    "literal" - {
        "should build by value" {
            // when
            val result = expression {
                literal(3)
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}literal": 3
                }
                """.trimIndent(),
            )
        }

        "should build by expression" {
            // when
            val result = expression {
                literal {
                    abs(3)
                }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}literal": {
                    "${'$'}abs": 3
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "cmp" - {
        "should build an expression" {
            // given
            val value = "path"

            // when
            val result = expression {
                cmp { of(value) compareTo "value" }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cmp": [
                    "${'$'}path",
                    "value"
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "eq" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                eq { of(value) equal value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}eq": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "gt" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                gt { of(value) greaterThan value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gt": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "gte" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                gte { of(value) greaterThanEqual value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}gte": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "lt" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                lt { of(value) lessThan value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lt": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "lte" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                lte { of(value) lessThanEqual value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lte": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "ne" - {
        "should build an expression" {
            // given
            val value = 1

            // when
            val result = expression {
                ne { of(value) notEqual value }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ne": [
                    1,
                    1
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
