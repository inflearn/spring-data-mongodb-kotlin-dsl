package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

class AutocompleteSearchOperationDslTest : FreeSpec({
    fun autocomplete(block: AutocompleteSearchOperationDsl.() -> Unit) =
        AutocompleteSearchOperationDsl().apply(block)

    "query" - {
        "should add a field by string" {
            // given
            val operation = autocomplete {
                query("query")
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "query": "query"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a query by multiple strings" {
            // given
            val operation = autocomplete {
                query("query1", "query2")
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "query": [
                      "query1",
                      "query2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a query by iterable string" {
            // given
            val operation = autocomplete {
                query(listOf("query1", "query2"))
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "query": [
                      "query1",
                      "query2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should add a field by string" {
            // given
            val operation = autocomplete {
                path("name")
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "path": "name"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            data class Collection(val property: String)
            val operation = autocomplete {
                path(Collection::property)
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "path": "property"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "fuzzy" - {
        "should build a fuzzy with maxEdits" {
            // given
            val operator = autocomplete {
                fuzzy(maxEdits = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "fuzzy": {
                      "maxEdits": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a fuzzy with prefixLength" {
            // given
            val operator = autocomplete {
                fuzzy(prefixLength = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "fuzzy": {
                      "prefixLength": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a fuzzy with prefixLength" {
            // given
            val operator = autocomplete {
                fuzzy(prefixLength = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "fuzzy": {
                      "prefixLength": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operation = autocomplete {
                score {
                    constant(1.0)
                }
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
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

    "tokenOrder" - {
        "should build a tokenOrder" {
            // given
            val operation = autocomplete {
                tokenOrder(TokenOrder.ANY)
            }

            // when
            val result = operation.build()

            // then
            result.shouldBeJson(
                """
                {
                  "autocomplete": {
                    "tokenOrder": "any"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
