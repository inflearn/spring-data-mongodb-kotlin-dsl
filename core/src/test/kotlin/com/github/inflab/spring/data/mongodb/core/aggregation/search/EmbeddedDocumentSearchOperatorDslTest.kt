package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class EmbeddedDocumentSearchOperatorDslTest : FreeSpec({
    fun embeddedDocument(block: EmbeddedDocumentSearchOperatorDsl.() -> Unit) =
        EmbeddedDocumentSearchOperatorDsl().apply(block)

    "operator" - {
        "should build an operator" {
            // given
            val operator = embeddedDocument {
                operator {
                    text {
                        path("fieldName")
                    }
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "embeddedDocument": {
                    "operator": {
                      "text": {
                        "path": "fieldName"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path by strings" {
            // given
            val operator = embeddedDocument {
                path("path1")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "embeddedDocument": {
                    "path": "path1"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by property" {
            // given
            data class TestCollection(val field: List<String>)
            val operator = embeddedDocument {
                path(TestCollection::field)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "embeddedDocument": {
                    "path": "field"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by nested property" {
            // given
            data class Child(val path: List<String>)
            data class Parent(val child: Child)
            val operator = embeddedDocument {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "embeddedDocument": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operator = embeddedDocument {
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
                  "embeddedDocument": {
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
