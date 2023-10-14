package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class GroupStageDslTest : FreeSpec({
    fun group(block: GroupStageDsl.() -> Unit) =
        GroupStageDsl().apply(block)

    "_id" - {
        "should build by null" {
            // when
            val stage = group {
                _idNull()
            }

            // then
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}group": {
                    "_id": null
                  }
                }
                """.trimIndent(),
            )
        }

        "should build by string" {
            // given
            val path = "path"
            val stage = group {
                _id(path)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}group": {
                    "_id": "${'$'}path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build by property" {
            // given
            data class Test(val property: String)
            val stage = group {
                _id(Test::property)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}group": {
                    "_id": "${'$'}property"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build by expression" {
            // given
            val stage = group {
                _id {
                    abs("field")
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}group": {
                    "_id": {
                      "${'$'}abs": "${'$'}field"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "accumulator" - {
        "should build by expression" {
            // given
            val stage = group {
                "amount" accumulator {
                    abs("field")
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}group": {
                    "amount": {
                      "${'$'}abs": "${'$'}field"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
