package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull

internal class CompoundSearchOperatorDslTest : FreeSpec({
    fun compound(block: CompoundSearchOperatorDsl.() -> Unit) =
        CompoundSearchOperatorDsl().apply(block)

    "empty" - {
        "should return null if there is no clause" {
            // given
            val operator = compound {
                score {
                    constant(2.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeNull()
        }
    }

    "minimumShouldMatch" - {
        "should build a given option" {
            // given
            val operator = compound {
                should {
                    text {
                        path("field")
                    }
                }
                minimumShouldMatch = 1
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "should": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ],
                    "minimumShouldMatch": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should not build if there is no should clause" {
            // given
            val operator = compound {
                must {
                    text {
                        path("field")
                    }
                }
                minimumShouldMatch = 1
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "must": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should add score block" {
            // given
            val operator = compound {
                should {
                    text {
                        path("field")
                    }
                }
                score {
                    constant(2.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "should": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ],
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

    "must" - {
        "should add must block" {
            // given
            val operator = compound {
                must {
                    text {
                        path("field")
                    }
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "must": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "mustNot" - {
        "should add mustNot block" {
            // given
            val operator = compound {
                mustNot {
                    text {
                        path("field")
                    }
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "mustNot": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "should" - {
        "should add should block" {
            // given
            val operator = compound {
                should {
                    text {
                        path("field")
                    }
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "should": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "filter" - {
        "should add filter block" {
            // given
            val operator = compound {
                filter {
                    text {
                        path("field")
                    }
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "filter": [
                      {
                        "text": {
                          "path": "field"
                        }
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
