package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class ShouldSearchClauseDslTest : FreeSpec({
    fun should(block: ShouldSearchClauseDsl.() -> Unit): ShouldSearchClauseDsl =
        ShouldSearchClauseDsl().apply(block)

    "minimumShouldMatch " - {
        "should set with given value" {
            // given
            val stage = should {
                minimumShouldMatch = 1
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "minimumShouldMatch": 1
                }
                """.trimIndent(),
            )
        }
    }

    "operator" - {
        "should add operator" {
            // given
            val stage = should {
                text { query("query") }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "query": "query"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
