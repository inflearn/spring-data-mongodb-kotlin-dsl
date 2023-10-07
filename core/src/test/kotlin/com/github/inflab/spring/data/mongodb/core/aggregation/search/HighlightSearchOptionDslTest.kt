package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class HighlightSearchOptionDslTest : FreeSpec({
    fun highlight(block: HighlightSearchOptionDsl.() -> Unit) =
        HighlightSearchOptionDsl().apply(block)

    "maxCharsToExamine" - {
        "should build an option" {
            // given
            val option = highlight {
                maxCharsToExamine = 2
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "maxCharsToExamine": 2
                }
                """.trimIndent(),
            )
        }
    }

    "maxNumPassages" - {
        "should build an option" {
            // given
            val option = highlight {
                maxNumPassages = 2
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "maxNumPassages": 2
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path" {
            // given
            val option = highlight {
                path {
                    +"path1"
                    "path2".ofWildcard()
                    "path3" multi "analyzer"
                }
            }

            // when
            val result = option.get()

            // then
            result.shouldBeJson(
                """
                {
                  "path": [
                    "path1",
                    {
                      "wildcard": "path2*"
                    },
                    {
                      "value": "path3",
                      "multi": "analyzer"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
