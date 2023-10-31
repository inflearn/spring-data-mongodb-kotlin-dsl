package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime

class InSearchOperatorDslTest : FreeSpec({
    fun `in`(block: InSearchOperatorDsl.() -> Unit): InSearchOperatorDsl =
        InSearchOperatorDsl().apply(block)

    "path" - {
        "should build a path by strings" {
            // given
            val operator = `in` {
                path("path")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "path": "path"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "value" - {
        listOf(true, false).forEach {
            "should build a value by boolean $it" {
                // given
                val operator = `in` {
                    value(it)
                }

                // when
                val result = operator.build()

                // then
                result.shouldBeJson(
                    """
                {
                  "in": {
                    "value": [
                      $it
                    ]
                  }
                }
                    """.trimIndent(),
                )
            }
        }

        "should build a value by multiple booleans " {
            // given
            val operator = `in` {
                value(true, false)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      true,
                      false
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by LocalDateTime" {
            // given
            val operator = `in` {
                value(LocalDateTime.of(2023, 10, 3, 17, 37, 26))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}date": "2023-10-03T17:37:26Z"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by multiple LocalDateTimes" {
            // given
            val operator = `in` {
                value(
                    LocalDateTime.of(2023, 10, 3, 17, 37, 26),
                    LocalDateTime.of(2023, 10, 31, 2, 15, 7),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}date": "2023-10-03T17:37:26Z"
                      },
                      {
                        "${'$'}date": "2023-10-31T02:15:07Z"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by LocalDate" {
            // given
            val operator = `in` {
                value(LocalDate.of(2023, 10, 3))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}date": "2023-10-03T00:00:00Z"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by multiple LocalDates" {
            // given
            val operator = `in` {
                value(
                    LocalDate.of(2023, 10, 3),
                    LocalDate.of(2023, 10, 31),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}date": "2023-10-03T00:00:00Z"
                      },
                      {
                        "${'$'}date": "2023-10-31T00:00:00Z"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by number" {
            // given
            val operator = `in` {
                value(1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      1
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by multiple numbers" {
            // given
            val operator = `in` {
                value(1, 2, 3)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      1,
                      2,
                      3
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by ObjectId" {
            // given
            val operator = `in` {
                value(ObjectId("651bd39c0ff5011eca8979ca"))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}oid": "651bd39c0ff5011eca8979ca"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a value by multiple ObjectId" {
            // given
            val operator = `in` {
                value(
                    ObjectId("651bd39c0ff5011eca8979ca"),
                    ObjectId("651bd39c0ff5011eca8979cb"),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "value": [
                      {
                        "${'$'}oid": "651bd39c0ff5011eca8979ca"
                      },
                      {
                        "${'$'}oid": "651bd39c0ff5011eca8979cb"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score by score option" {
            // given
            val operator = `in` {
                score {
                    constant(1.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "in": {
                    "score": {
                      "constant": {
                        "value": 1.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
