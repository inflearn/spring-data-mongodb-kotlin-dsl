package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate
import java.time.LocalDateTime

internal class RangeSearchOperatorDslTest : FreeSpec({
    fun range(block: RangeSearchOperatorDsl.() -> Unit) =
        RangeSearchOperatorDsl().apply(block)

    "lt & lte" - {
        "should set value with number" {
            // given
            val operator = range {
                lt(1.5)
                lte(2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "lt": 1.5,
                    "lte": 2
                  }
                }
                """.trimIndent(),
            )
        }

        "should set value with temporal" {
            // given
            val operator = range {
                lt(LocalDateTime.of(2021, 2, 1, 10, 2, 3))
                lte(LocalDate.of(2022, 4, 5))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "lt": {
                      "${'$'}date": "2021-02-01T10:02:03Z"
                    },
                    "lte": {
                      "${'$'}date": "2022-04-05T00:00:00Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "gt & gte" - {
        "should set value with number" {
            // given
            val operator = range {
                gt(1.5)
                gte(2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "gt": 1.5,
                    "gte": 2
                  }
                }
                """.trimIndent(),
            )
        }

        "should set value with temporal" {
            // given
            val operator = range {
                gt(LocalDateTime.of(2021, 2, 1, 10, 2, 3))
                gte(LocalDate.of(2022, 4, 5))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "gt": {
                      "${'$'}date": "2021-02-01T10:02:03Z"
                    },
                    "gte": {
                      "${'$'}date": "2022-04-05T00:00:00Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should set path by strings" {
            // given
            val operator = range {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by number properties" {
            // given
            data class TestCollection(val path1: Int, val path2: Double)
            val operator = range {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by temporal properties" {
            // given
            data class TestCollection(val path1: LocalDateTime, val path2: LocalDate)
            val operator = range {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "range": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should set score" {
            // given
            val operator = range {
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
                  "range": {
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
