package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class SearchMetaStageDslTest : FreeSpec({
    fun searchMeta(block: SearchMetaStageDsl.() -> Unit): SearchMetaStageDsl =
        SearchMetaStageDsl().apply(block)

    "index" - {
        "should build an index with given name" {
            // given
            val stage = searchMeta {
                index = "indexName"
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}searchMeta": {
                    "index": "indexName"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "lowerBoundCount" - {
        "should build a lowerBoundCount stage with given threshold" {
            // given
            val stage = searchMeta {
                lowerBoundCount(100)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}searchMeta": {
                    "count": {
                      "type": "lowerBound",
                      "threshold": 100
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a lowerBoundCount stage without threshold" {
            // given
            val stage = searchMeta {
                lowerBoundCount()
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}searchMeta": {
                    "count": {
                      "type": "lowerBound"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "totalCount" - {
        "should build a totalCount stage" {
            // given
            val stage = searchMeta {
                totalCount()
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}searchMeta": {
                    "count": {
                      "type": "total"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
