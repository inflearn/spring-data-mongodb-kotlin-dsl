package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class AddFieldsStageDslTest : FreeSpec({

    fun addFields(configuration: AddFieldsStageDsl.() -> Unit): AddFieldsStageDsl =
        AddFieldsStageDsl().apply(configuration)

    "set" - {
        "should set field with path" {
            // given
            data class Test(val targetPath: Int)
            val stage = addFields {
                "a" set Test::targetPath
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
                    "a": "${'$'}targetPath"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with path" {
            // given
            data class Test(val sourcePath: Int, val targetPath: Int)
            val stage = addFields {
                Test::targetPath set Test::sourcePath
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
                    "targetPath": "${'$'}sourcePath"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field with aggregation expression" {
            // given
            data class Test(val targetPath: Int)
            val stage = addFields {
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
                  "${'$'}addFields": {
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
            val stage = addFields {
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
                  "${'$'}addFields": {
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
            val stage = addFields {
                "a" set 1
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
                    "a": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with value" {
            // given
            data class Test(val targetPath: Int)
            val stage = addFields {
                Test::targetPath set 1
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
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
            val stage = addFields {
                "a" setByField "b"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
                    "a": "${'$'}b"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set field path with value of other field" {
            // given
            data class Test(val targetPath: Int)
            val stage = addFields {
                Test::targetPath setByField "b"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}addFields": {
                    "targetPath": "${'$'}b"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
