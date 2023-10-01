package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class ScoreFunctionSearchOptionDslTest : FreeSpec({
    fun scoreFunction(block: ScoreFunctionSearchOptionDsl.() -> ScoreFunctionSearchOptionDsl.Expression) =
        ScoreFunctionSearchOptionDsl().block()

    "score" - {
        "should build a score" {
            // given
            val option = scoreFunction {
                score()
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "score": "relevance"
                }
                """.trimIndent(),
            )
        }
    }

    "constant" - {
        "should build a value with given one" {
            // given
            val option = scoreFunction {
                constant(2.0)
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "constant": 2.0
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path from string field" {
            // given
            val option = scoreFunction {
                path("path")
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "path": {
                    "value": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path from KProperty field" {
            // given
            val testObject = object : Any() { val path = 123 }
            val option = scoreFunction {
                path(testObject::path)
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "path": {
                    "value": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path from string field with undefined" {
            // given
            val option = scoreFunction {
                path("path", 0.0)
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "path": {
                    "value": "path",
                    "undefined": 0.0
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path from KProperty field with undefined" {
            // given
            val testObject = object : Any() { val path = 123 }
            val option = scoreFunction {
                path(testObject::path, 0.0)
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "path": {
                    "value": "path",
                    "undefined": 0.0
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "gauss" - {
        "should build a gauss" {
            // given
            val option = scoreFunction {
                gauss(
                    decay = 0.5,
                    offset = 1.0,
                    origin = 2.0,
                    path = path("path"),
                    scale = 3.0,
                )
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "gauss": {
                    "decay": 0.5,
                    "offset": 1.0,
                    "origin": 2.0,
                    "scale": 3.0,
                    "path": {
                      "value": "path"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "log" - {
        "should build a log" {
            // given
            val option = scoreFunction {
                log(path("path"))
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "log": {
                    "path": {
                      "value": "path"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "log1p" - {
        "should build a log1p" {
            // given
            val option = scoreFunction {
                log1p(path("path"))
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "log1p": {
                    "path": {
                      "value": "path"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "add" - {
        "should add expressions" {
            // given
            val option = scoreFunction {
                add(
                    constant(1.0),
                    path("path"),
                )
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "add": [
                    {
                      "constant": 1.0
                    },
                    {
                      "path": {
                        "value": "path"
                      }
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "multiply" - {
        "should multiply expressions" {
            // given
            val option = scoreFunction {
                multiply(
                    constant(1.0),
                    path("path"),
                )
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "multiply": [
                    {
                      "constant": 1.0
                    },
                    {
                      "path": {
                        "value": "path"
                      }
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "nested expressions" - {
        "should build a nested expressions" {
            // given
            val option = scoreFunction {
                add(
                    log1p(path("path")),
                    multiply(
                        constant(2.0),
                        path("path"),
                    ),
                )
            }

            // when
            val result = option.toDocument()

            // then
            result.shouldBeJson(
                """
                {
                  "add": [
                    {
                      "log1p": {
                        "path": {
                          "value": "path"
                        }
                      }
                    },
                    {
                      "multiply": [
                        {
                          "constant": 2.0
                        },
                        {
                          "path": {
                            "value": "path"
                          }
                        }
                      ]
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
