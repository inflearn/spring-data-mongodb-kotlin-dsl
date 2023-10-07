package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class PhraseSearchOperatorDslTest : FreeSpec({
    fun phrase(block: PhraseSearchOperatorDsl.() -> Unit) =
        PhraseSearchOperatorDsl().apply(block)

    "slop" - {
        "should build a slop" {
            // given
            val operator = phrase {
                slop = 1
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
                    "slop": 1
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "query" - {
        "should build a query by string" {
            // given
            val operator = phrase {
                query("query")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
                    "query": "query"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a query by multiple strings" {
            // given
            val operator = phrase {
                query("query1", "query2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
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
            val operator = phrase {
                query(listOf("query1", "query2"))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
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
            val operator = phrase {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
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
            val operator = phrase {
                path(TestCollection::path1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
                    "path": "path1"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by multiple properties" {
            // given
            data class TestCollection(val path1: String, val path2: String)
            val operator = phrase {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
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
            val operator = phrase {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by option block" {
            // given
            data class TestCollection(val path1: String, val path2: List<String>)
            val operator = phrase {
                path {
                    +"path0"
                    +TestCollection::path1
                    +TestCollection::path2
                    "path3".ofWildcard()
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "phrase": {
                    "path": [
                      "path0",
                      "path1",
                      "path2",
                      {
                        "wildcard": "path3*"
                      }
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
            val operator = phrase {
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
                  "phrase": {
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
