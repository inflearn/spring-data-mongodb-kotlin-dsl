package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators

internal class IfNullExpressionDslTest : FreeSpec({
    fun ifNull(block: IfNullExpressionDsl.() -> ConditionalOperators.IfNull) =
        IfNullExpressionDsl().block()

    "case" - {
        "should create a condition by string" {
            // given
            val field = "path"

            // when
            val result = ifNull {
                case(field) thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "$$field",
                    false
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by property" {
            // given
            data class Test(val property: Boolean?)

            // when
            val result = ifNull {
                case(Test::property) thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "${'$'}property",
                    false
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by expression" {
            // when
            val result = ifNull {
                case {
                    abs(3)
                } thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    {
                      "${'$'}abs": 3
                    },
                    false
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "or" - {
        "should create a condition by string" {
            // given
            val field = "path"

            // when
            val result = ifNull {
                case(field) or field thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "$$field",
                    "$$field",
                    false
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by property" {
            // given
            data class Test(val property: Boolean?)

            // when
            val result = ifNull {
                case(Test::property) or Test::property thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "${'$'}property",
                    "${'$'}property",
                    false
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by expression" {
            // when
            val result = ifNull {
                case {
                    abs(3)
                } or {
                    abs(3)
                } thenValue false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    {
                      "${'$'}abs": 3
                    },
                    {
                      "${'$'}abs": 3
                    },
                    false
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "then" - {
        "should create a condition by string" {
            // given
            val field = "path"

            // when
            val result = ifNull {
                case(field) then field
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "$$field",
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by property" {
            // given
            data class Test(val property: Boolean?)

            // when
            val result = ifNull {
                case(Test::property) then Test::property
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    "${'$'}property",
                    "${'$'}property"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a condition by expression" {
            // when
            val result = ifNull {
                case {
                    abs(3)
                } then {
                    abs(3)
                }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}ifNull": [
                    {
                      "${'$'}abs": 3
                    },
                    {
                      "${'$'}abs": 3
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
