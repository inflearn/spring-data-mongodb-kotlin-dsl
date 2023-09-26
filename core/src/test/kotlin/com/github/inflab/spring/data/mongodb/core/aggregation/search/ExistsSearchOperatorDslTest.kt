package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class ExistsSearchOperatorDslTest : FreeSpec({
    fun exists(block: ExistsSearchOperatorDsl.() -> Unit): ExistsSearchOperatorDsl =
        ExistsSearchOperatorDsl().apply(block)

    "path" - {
        "should set path by strings" {
            // given
            val operator = exists {
                path("path")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "exists": {
                    "path": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by property" {
            // given
            data class Test(
                val property: String,
            )

            val operator = exists {
                path(Test::property)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "exists": {
                    "path": "property"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should set score by score option" {
            // given
            val operator = exists {
                score {
                    boost(1.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "exists": {
                    "score": {
                      "boost": {
                        "value": 1.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
