package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class ScoreSearchOptionDslTest : FreeSpec({
    fun score(block: ScoreSearchOptionDsl.() -> Unit): ScoreSearchOptionDsl =
        ScoreSearchOptionDsl().apply(block)

    "boost" - {
        "should build a value with given one" {
            // given
            val option = score {
                boost(2.0)
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "boost": {
                    "value": 2.0
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path from string field" {
            // given
            val option = score {
                boost("path")
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "boost": {
                    "path": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path from KProperty field" {
            // given
            val testObject = object : Any() { val path = 123 }
            val option = score {
                boost(testObject::path)
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "boost": {
                    "path": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path with undefined value" {
            // given
            val option = score {
                boost("path", undefined = 0.0)
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "boost": {
                    "path": "path",
                    "undefined": 0.0
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "constant" - {
        "should build a value with given one" {
            // given
            val option = score {
                constant(2.0)
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "constant": {
                    "value": 2.0
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "embedded" - {
        "should build an aggregate with strategy" {
            // given
            val option = score {
                embedded(ScoreEmbeddedAggregateStrategy.MEAN)
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "embedded": {
                    "aggregate": "mean"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build an outerScore with default strategy" {
            // given
            val option = score {
                embedded {
                    boost(2.0)
                }
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "embedded": {
                    "aggregate": "sum",
                    "outerScore": {
                      "boost": {
                        "value": 2.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "function" - {
        "should build a function with given one" {
            // given
            val option = score {
                function {
                    expression = constant(3.0)
                }
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "function": {
                    "constant": 3.0
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
