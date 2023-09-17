package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mapping.div

class TextSearchOperatorDslTest : FreeSpec({
    fun text(block: TextSearchOperatorDsl.() -> Unit): TextSearchOperatorDsl =
        TextSearchOperatorDsl().apply(block)

    "query" - {
        "should set query by string" {
            // given
            val operator = text {
                query("query")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "query": [
                      "query"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should set query by multiple strings" {
            // given
            val operator = text {
                query("query1", "query2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
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
        "should set path by strings" {
            // given
            val operator = text {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by multiple properties" {
            // given
            data class TestCollection(val path1: String, val path2: String)
            val operator = text {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by nested property" {
            // given
            data class Child(val path: String)
            data class Parent(val child: Child)
            val operator = text {
                path(Parent::child / Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "path": [
                      "child.path"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "fuzzy" - {
        "should set fuzzy with maxEdits" {
            // given
            val operator = text {
                fuzzy(maxEdits = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "fuzzy": {
                      "maxEdits": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set fuzzy with prefixLength" {
            // given
            val operator = text {
                fuzzy(prefixLength = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "fuzzy": {
                      "prefixLength": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set fuzzy with prefixLength" {
            // given
            val operator = text {
                fuzzy(prefixLength = 1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
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
        "should set score" {
            // given
            val operator = text {
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
                  "text": {
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

    "synonyms" - {
        "should set synonyms" {
            // given
            val operator = text {
                synonyms("synonyms")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "text": {
                    "synonyms": "synonyms"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
