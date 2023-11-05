package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.util.OptionalInt.of

class ReplaceRootDslTest : FreeSpec({

    fun replaceRoot(configuration: ReplaceRootDsl.() -> Unit): ReplaceRootDsl =
        ReplaceRootDsl().apply(configuration)

    "newRoot" - {
        "ReplacementDocumentOperationDsl" - {
            "should set field with aggregation expression" {
                // given
                val stage = replaceRoot {
                    newRoot {
                        "a" set {
                            add { of(1) and 2 }
                        }
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                val stage = replaceRoot {
                    newRoot {
                        "a" set Test::targetPath
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                // given
                val stage = replaceRoot {
                    newRoot {
                        "a" set 1
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                // given
                val stage = replaceRoot {
                    newRoot {
                        "a" setByField "b"
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set {
                            add { of(1) and 2 and Test::sourcePath }
                        }
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set Test::sourcePath
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                val stage = replaceRoot {
                    newRoot {
                        Test::targetPath set 1
                    }
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                // given
                val stage = replaceRoot {
                    newRoot("a")
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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
                val stage = replaceRoot {
                    newRoot(Test::a)
                }

                // when
                val result = stage.get()

                // then
                result.shouldBeJson(
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

    "Expressions" - {
        "should set new root from aggregation expression" {
            // given
            val stage = replaceRoot {
                expressions {
                    add { of(1) and 2 }
                }
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
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
