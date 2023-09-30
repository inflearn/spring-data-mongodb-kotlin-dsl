package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class WildcardSearchOperatorDslTest : FreeSpec({
    fun wildcard(block: WildcardSearchOperatorDsl.() -> Unit) =
        WildcardSearchOperatorDsl().apply(block)

    "allowAnalyzedField" - {
        "should build an option with given value" {
            // given
            val operator = wildcard {
                allowAnalyzedField = true
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "allowAnalyzedField": true
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "query" - {
        "should build a query by string" {
            // given
            val operator = wildcard {
                query("query")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "query": "query"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a query by multiple strings" {
            // given
            val operator = wildcard {
                query("query1", "query2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
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
        "should build a path by strings" {
            // given
            val operator = wildcard {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by iterable property" {
            // given
            data class TestCollection(val path1: List<String>)
            val operator = wildcard {
                path(TestCollection::path1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "path": "path1"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by multiple properties" {
            // given
            data class TestCollection(val path1: String, val path2: String)
            val operator = wildcard {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by nested property" {
            // given
            data class Child(val path: String)
            data class Parent(val child: Child)
            val operator = wildcard {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by option block" {
            // given
            data class TestCollection(val path1: String, val path2: List<String>)
            val operator = wildcard {
                path {
                    +"path0"
                    +TestCollection::path1
                    +TestCollection::path2
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "wildcard": {
                    "path": [
                      "path0",
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operator = wildcard {
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
                  "wildcard": {
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
