package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation

internal class ReplaceRootDslTest : FreeSpec({

    fun replaceRoot(configuration: ReplaceRootDsl.() -> ReplaceRootOperation): ReplaceRootOperation =
        ReplaceRootDsl().configuration()

    "newRoot" - {
        "ReplacementDocumentOperationDsl" - {
            "should set field with aggregation expression" {
                // given, when
                val stage = replaceRoot {
                    newRoot {
                        "a" set {
                            add { of(1) and 2 }
                        }
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a": {
                        "${'$'}add": [
                          1,
                          2
                        ]
                      }
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field with path" {
                // given
                data class Test(val a: Int, val targetPath: Int)

                // when
                val stage = replaceRoot {
                    newRoot {
                        "a" set Test::targetPath
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a": "${'$'}targetPath"
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field with value" {
                // given, when
                val stage = replaceRoot {
                    newRoot {
                        "a" set 1
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a": 1
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field with value of other field" {
                // given, when
                val stage = replaceRoot {
                    newRoot {
                        "a" setByField "b"
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a": "${'$'}b"
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set nested field" {
                // given
                data class Test(val a: Long, val b: Long, val c: Long, val d: Long)

                // when
                val stage = replaceRoot {
                    newRoot {
                        nested("a") {
                            "b" set Test::d
                            "c" set {
                                add { of(1) and 2 }
                            }
                            "d" setByField "c"
                            nested("e") {
                                "f" set 1
                            }
                        }
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a.b": "${'$'}d",
                      "a.c": {
                        "${'$'}add": [
                          1,
                          2
                        ]
                      },
                      "a.d": "${'$'}c",
                      "a.e.f": 1
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field path with aggregation expression" {
                // given
                data class Test(val sourcePath: Int, val targetPath: Int)

                // when
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set {
                            add { of(1) and 2 and Test::sourcePath }
                        }
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "targetPath": {
                        "${'$'}add": [
                          1,
                          2,
                          "${'$'}sourcePath"
                        ]
                      }
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field path with path" {
                // given
                data class Test(val sourcePath: Int, val targetPath: Int)

                // when
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set Test::sourcePath
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "targetPath": "${'$'}sourcePath"
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set field path with value" {
                // given
                data class Test(val sourcePath: Int, val targetPath: Int)

                // when
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set 1
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "targetPath": 1
                    }
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set nested field path" {
                // given
                data class Test(val a: Long, val b: Long, val c: Long, val d: Long)

                // when
                val stage = replaceRoot {
                    newRoot {
                        nested(Test::a) {
                            Test::b set Test::d
                            Test::c set {
                                add { of(1) and 2 }
                            }
                            Test::d setByField "c"
                            nested("e") {
                                "f" set 1
                            }
                        }
                    }
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "a.b": "${'$'}d",
                      "a.c": {
                        "${'$'}add": [
                          1,
                          2
                        ]
                      },
                      "a.d": "${'$'}c",
                      "a.e.f": 1
                    }
                  }
                }
                    """.trimIndent(),
                )
            }
        }

        "FieldReplacement" - {
            "should set new root from a field" {
                // given, when
                val stage = replaceRoot {
                    newRoot("a")
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": "${'$'}a"
                  }
                }
                    """.trimIndent(),
                )
            }

            "should set new root from a field path" {
                // given
                data class Test(val a: Long)

                // when
                val stage = replaceRoot {
                    newRoot(Test::a)
                }

                // then
                stage.shouldBeJson(
                    """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": "${'$'}a"
                  }
                }
                    """.trimIndent(),
                )
            }
        }
    }

    "AggregationExpressions" - {
        "should set new root from aggregation expression" {
            // given, when
            val stage = replaceRoot {
                newRoot<AggregationExpression> {
                    add { of(1) and 2 }
                }
            }

            // then
            stage.shouldBeJson(
                """
                {
                  "${'$'}replaceRoot": {
                    "newRoot": {
                      "${'$'}add": [
                        1,
                        2
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
