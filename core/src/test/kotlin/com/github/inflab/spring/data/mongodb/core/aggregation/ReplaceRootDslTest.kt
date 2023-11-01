package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

class ReplaceRootDslTest : FreeSpec({

    fun replaceRoot(configuration: ReplaceRootDsl.() -> Unit): ReplaceRootDsl =
        ReplaceRootDsl().apply(configuration)

    "NewRootDsl" - {
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
    }
})
