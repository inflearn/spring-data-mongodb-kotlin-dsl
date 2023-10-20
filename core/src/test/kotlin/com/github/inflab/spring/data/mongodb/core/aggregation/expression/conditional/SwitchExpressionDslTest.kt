package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class SwitchExpressionDslTest : FreeSpec({
    fun switch(block: SwitchExpressionDsl.() -> Unit) =
        SwitchExpressionDsl().apply(block).build()

    "branch" - {
        "should create a branch expression" {
            // when
            val result = switch {
                branch({ exp(2) }, 1)
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}switch": {
                    "branches": [
                      {
                        "case": {
                          "${'$'}exp": 2
                        },
                        "then": 1
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should create many branch expression" {
            // when
            val result = switch {
                branch({ exp(2) }, 1)
                branch({ exp(2) }, 1)
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}switch": {
                    "branches": [
                      {
                        "case": {
                          "${'$'}exp": 2
                        },
                        "then": 1
                      },
                      {
                        "case": {
                          "${'$'}exp": 2
                        },
                        "then": 1
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "default" - {
        "should create a default expression" {
            // when
            val result = switch {
                branch({ exp(2) }, 1)
                default(1)
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}switch": {
                    "branches": [
                      {
                        "case": {
                          "${'$'}exp": 2
                        },
                        "then": 1
                      }
                    ],
                    "default": 1
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
