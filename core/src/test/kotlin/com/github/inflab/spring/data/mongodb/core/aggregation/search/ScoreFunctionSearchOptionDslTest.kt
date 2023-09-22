package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class ScoreFunctionSearchOptionDslTest : FreeSpec({
    fun scoreFunction(block: ScoreFunctionSearchOptionDsl.() -> Unit): ScoreFunctionSearchOptionDsl =
        ScoreFunctionSearchOptionDsl().apply(block)

    "error" - {
        "should throw exception when expression is not set" {
            // given
            val option = scoreFunction { }

            // when
            val exception = shouldThrow<IllegalStateException> { option.build() }

            // then
            exception.message shouldBe "Expression must not be null"
        }
    }

    "score" - {
        "should set score" {
            // given
            val option = scoreFunction {
                expression = score()
            }

            // when
            val result = option.build()

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
        "should set value with given one" {
            // given
            val option = scoreFunction {
                expression = constant(2.0)
            }

            // when
            val result = option.build()

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
        "should set path from string field" {
            // given
            val option = scoreFunction {
                expression = path("path")
            }

            // when
            val result = option.build()

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

        "should set path from KProperty field" {
            // given
            val testObject = object : Any() { val path = 123 }
            val option = scoreFunction {
                expression = path(testObject::path)
            }

            // when
            val result = option.build()

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

        "should set path from string field with undefined" {
            // given
            val option = scoreFunction {
                expression = path("path", 0.0)
            }

            // when
            val result = option.build()

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

        "should set path from KProperty field with undefined" {
            // given
            val testObject = object : Any() { val path = 123 }
            val option = scoreFunction {
                expression = path(testObject::path, 0.0)
            }

            // when
            val result = option.build()

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
        "should set gauss" {
            // given
            val option = scoreFunction {
                expression = gauss(
                    decay = 0.5,
                    offset = 1.0,
                    origin = 2.0,
                    path = path("path"),
                    scale = 3.0,
                )
            }

            // when
            val result = option.build()

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
        "should set log" {
            // given
            val option = scoreFunction {
                expression = log(path("path"))
            }

            // when
            val result = option.build()

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
        "should set log1p" {
            // given
            val option = scoreFunction {
                expression = log1p(path("path"))
            }

            // when
            val result = option.build()

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
                expression = add(
                    constant(1.0),
                    path("path"),
                )
            }

            // when
            val result = option.build()

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
                expression = multiply(
                    constant(1.0),
                    path("path"),
                )
            }

            // when
            val result = option.build()

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
        "should set nested expressions" {
            // given
            val option = scoreFunction {
                expression = add(
                    log1p(path("path")),
                    multiply(
                        constant(2.0),
                        path("path"),
                    ),
                )
            }

            // when
            val result = option.build()

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
