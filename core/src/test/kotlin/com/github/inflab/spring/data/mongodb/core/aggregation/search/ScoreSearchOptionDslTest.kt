package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

class ScoreSearchOptionDslTest : FreeSpec({
    fun score(block: ScoreSearchOptionDsl.() -> Unit): ScoreSearchOptionDsl =
        ScoreSearchOptionDsl().apply(block)

    "boost" - {
        "should set value with given one" {
            // given
            val option = score {
                boost(2.0)
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "boost": {
                      "value": 2.0
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path from string field" {
            // given
            val option = score {
                boost("path")
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "boost": {
                      "path": "path"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path from KProperty field" {
            // given
            val testObject = object : Any() { val path = "path" }
            val option = score {
                boost(testObject::path)
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "boost": {
                      "path": "path"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path with undefined value" {
            // given
            val option = score {
                boost("path", undefined = 0.0)
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "boost": {
                      "path": "path",
                      "undefined": 0.0
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "constant" - {
        "should set value with given one" {
            // given
            val option = score {
                constant(2.0)
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "constant": {
                      "value": 2.0
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "embedded" - {
        "should set aggregate with strategy" {
            // given
            val option = score {
                embedded(ScoreEmbeddedAggregateStrategy.MEAN)
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "embedded": {
                      "aggregate": "mean"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set outerScore with default strategy" {
            // given
            val option = score {
                embedded {
                    boost(2.0)
                }
            }

            // when
            val result = option.build()

            // then
            result.shouldBeJson(
                """
                {
                  "score": {
                    "embedded": {
                      "aggregate": "sum",
                      "outerScore": {
                        "boost": {
                          "value": 2.0
                        }
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
