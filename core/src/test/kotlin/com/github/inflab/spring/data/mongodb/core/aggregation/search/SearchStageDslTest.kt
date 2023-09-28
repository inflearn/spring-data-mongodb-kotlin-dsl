package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class SearchStageDslTest : FreeSpec({
    fun search(block: SearchStageDsl.() -> Unit): SearchStageDsl =
        SearchStageDsl().apply(block)

    "index" - {
        "should build an index with given name" {
            // given
            val stage = search {
                index = "indexName"
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}search": {
                    "index": "indexName"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "returnStoredSource" - {
        listOf(true, false).forEach {
            "should build a returnStoredSource stage with $it" {
                // given
                val stage = search {
                    returnStoredSource = it
                }

                // when
                val result = stage.build()

                // then
                result.shouldBeJson(
                    """
                    {
                      "${"$"}search": {
                        "returnStoredSource": $it
                      }
                    }
                    """.trimIndent(),
                )
            }
        }
    }

    "scoreDetails" - {
        listOf(true, false).forEach {
            "should build a scoreDetails stage with $it" {
                // given
                val stage = search {
                    scoreDetails = it
                }

                // when
                val result = stage.build()

                // then
                result.shouldBeJson(
                    """
                    {
                      "${"$"}search": {
                        "scoreDetails": $it
                      }
                    }
                    """.trimIndent(),
                )
            }
        }
    }

    "lowerBoundCount" - {
        "should build a lowerBoundCount stage with given threshold" {
            // given
            val stage = search {
                lowerBoundCount(100)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}search": {
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
            val stage = search {
                lowerBoundCount()
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}search": {
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
            val stage = search {
                totalCount()
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}search": {
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
