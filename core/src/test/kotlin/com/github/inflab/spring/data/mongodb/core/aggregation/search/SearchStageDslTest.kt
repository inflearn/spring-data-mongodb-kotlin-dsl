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
            "should build a returnStoredSource option with $it" {
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
            "should build a scoreDetails option with $it" {
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
        "should build a lowerBoundCount option with given threshold" {
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

        "should build a lowerBoundCount option without threshold" {
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
        "should build a totalCount option" {
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

    "highlight" - {
        "should build a highlight option" {
            // given
            val stage = search {
                highlight {
                    path {
                        +"title"
                    }
                    maxNumPassages = 5
                    maxCharsToExamine = 100
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "${"$"}search": {
                    "highlight": {
                      "path": "title",
                      "maxNumPassages": 5,
                      "maxCharsToExamine": 100
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
