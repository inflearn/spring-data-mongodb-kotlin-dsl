package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

class SetStageDslTest : FreeSpec({
    fun set(configuration: SetStageDsl.() -> Unit): SetStageDsl =
        SetStageDsl().apply(configuration)

    "set" - {
        "should set field with path" {
            // given
            data class Test(val targetPath: Int)
            val stage = set {
                "a" set Test::targetPath
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "a": "${'$'}targetPath"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with path" {
            // given
            data class Test(val sourcePath: Int, val targetPath: Int)
            val stage = set {
                Test::targetPath set Test::sourcePath
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "targetPath": "${'$'}sourcePath"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field with aggregation expression" {
            // given
            data class Test(val targetPath: Int)
            val stage = set {
                "a" set {
                    add { of(1) and 2 and Test::targetPath }
                }
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "a": {
                      "${'$'}add": [
                        1,
                        2,
                        "${'$'}targetPath"
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with aggregation expression" {
            // given
            data class Test(val sourcePath: Int, val targetPath: Int)
            val stage = set {
                Test::targetPath set {
                    add { of(1) and 2 and Test::sourcePath }
                }
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "targetPath": {
                      "${'$'}add": [
                        1,
                        2,
                        "${'$'}sourcePath"
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field with value" {
            // given
            val stage = set {
                "a" set 1
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "a": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with value" {
            // given
            data class Test(val targetPath: Int)
            val stage = set {
                Test::targetPath set 1
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "targetPath": 1
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "setByField" - {
        "should set field with value of other field" {
            // given
            val stage = set {
                "a" setByField "b"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "a": "${'$'}b"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with value of other field" {
            // given
            data class Test(val targetPath: Int)
            val stage = set {
                Test::targetPath setByField "b"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}set": {
                    "targetPath": "${'$'}b"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
