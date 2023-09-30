package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull

internal class UnwindStageDslTest : FreeSpec({
    fun unwind(block: UnwindStageDsl.() -> Unit) =
        UnwindStageDsl().apply(block)

    "path" - {
        "should add a field by string" {
            // given
            val stage = unwind {
                path("field")
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unwind": "${'$'}field"
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            data class Collection(val property: String)
            val stage = unwind {
                path(Collection::property)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unwind": "${'$'}property"
                }
                """.trimIndent(),
            )
        }
    }

    "includeArrayIndex" - {
        "should build an array index option" {
            // given
            val stage = unwind {
                path("field")
                includeArrayIndex = "index"
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unwind": {
                    "path": "${'$'}field",
                    "includeArrayIndex": "index",
                    "preserveNullAndEmptyArrays": false
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "preserveNullAndEmptyArrays" - {
        "should build a preserve null and empty arrays option" {
            // given
            val stage = unwind {
                path("field")
                preserveNullAndEmptyArrays = true
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unwind": {
                    "path": "${'$'}field",
                    "preserveNullAndEmptyArrays": true
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
