package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class QueryStringSearchOperatorDslTest : FreeSpec({
    fun queryString(block: QueryStringSearchOperatorDsl.() -> Unit) =
        QueryStringSearchOperatorDsl().apply(block)

    "defaultPath" - {
        "should build a default path by string" {
            // given
            val operator = queryString {
                defaultPath("path")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "queryString": {
                    "defaultPath": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a default path by property" {
            // given
            data class Collection(val field: List<String?>?)
            val operator = queryString {
                defaultPath(Collection::field)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "queryString": {
                    "defaultPath": "field"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "query" - {
        "should build a query" {
            // given
            val operator = queryString {
                query {
                    text("search")
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "queryString": {
                    "query": "\"search\""
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operator = queryString {
                score {
                    constant(1.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "queryString": {
                    "score": {
                      "constant": {
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
