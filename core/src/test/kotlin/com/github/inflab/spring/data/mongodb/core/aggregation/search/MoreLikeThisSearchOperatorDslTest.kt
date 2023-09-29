package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.bson.Document

internal class MoreLikeThisSearchOperatorDslTest : FreeSpec({
    fun moreLikeThis(block: MoreLikeThisSearchOperatorDsl.() -> Unit) =
        MoreLikeThisSearchOperatorDsl().apply(block)

    "like" - {
        "should build a like option by args" {
            // given
            val operator = moreLikeThis {
                like(
                    Document("foo", "bar"),
                    Document("baz", "qux"),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "moreLikeThis": {
                    "like": [
                      {
                        "foo": "bar"
                      },
                      {
                        "baz": "qux"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a like option by list" {
            // given
            val operator = moreLikeThis {
                like(
                    listOf(
                        Document("foo", "bar"),
                        Document("baz", "qux"),
                    ),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "moreLikeThis": {
                    "like": [
                      {
                        "foo": "bar"
                      },
                      {
                        "baz": "qux"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score option" {
            // given
            val operator = moreLikeThis {
                score {
                    constant(2.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "moreLikeThis": {
                    "score": {
                      "constant": {
                        "value": 2.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
