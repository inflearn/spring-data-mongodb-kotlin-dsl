package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class CompoundSearchOperatorDslTest : FreeSpec({
    fun compound(block: CompoundSearchOperatorDsl.() -> Unit) =
        CompoundSearchOperatorDsl().apply(block)

    "minimumShouldMatch" - {
        "should set with given value" {
            // given
            val operator = compound {
                minimumShouldMatch = 2
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "compound": {
                    "minimumShouldMatch": 2
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
                  "compound": {
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
