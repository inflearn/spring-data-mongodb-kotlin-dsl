package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate
import java.time.LocalDateTime

class FacetCollectorDslTest : FreeSpec({
    fun facet(block: FacetCollectorDsl.() -> Unit) =
        FacetCollectorDsl().apply(block)

    "operator" - {
        "should set operator" {
            // given
            val stage = facet {
                operator {
                    text {
                        path("fieldName")
                    }
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "operator": {
                      "text": {
                        "path": "fieldName"
                      }
                    },
                    "facets": {}
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "stringFacet" - {
        "should set with string value" {
            // given
            val stage = facet {
                "name".stringFacet(path = "path")
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "string",
                        "path": "path"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set with property" {
            // given
            data class Test(val path: String)
            val stage = facet {
                "name".stringFacet(path = Test::path)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "string",
                        "path": "path"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set with numBuckets" {
            // given
            val stage = facet {
                "name".stringFacet(path = "path", numBuckets = 100)
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "string",
                        "path": "path",
                        "numBuckets": 100
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "numberFacet" - {
        "should set with property" {
            // given
            data class Test(val path: Int)
            val stage = facet {
                "name".numberFacet(path = Test::path, boundaries = listOf(1, 2, 3))
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "number",
                        "path": "path",
                        "boundaries": [
                          1,
                          2,
                          3
                        ]
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set with default" {
            // given
            val stage = facet {
                "name".numberFacet(path = "path", boundaries = listOf(1, 2, 3), default = "default")
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "number",
                        "path": "path",
                        "boundaries": [
                          1,
                          2,
                          3
                        ],
                        "default": "default"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "dateFacet" - {
        "should set with property" {
            // given
            data class Test(val path: LocalDateTime)
            val stage = facet {
                "name".dateFacet(path = Test::path, boundaries = listOf(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1)))
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "date",
                        "path": "path",
                        "boundaries": [
                          {
                            "${"$"}date": "2021-01-01T00:00:00Z"
                          },
                          {
                            "${"$"}date": "2021-02-01T00:00:00Z"
                          }
                        ]
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set with default" {
            // given
            val stage = facet {
                "name".dateFacet(path = "path", boundaries = listOf(LocalDateTime.of(2023, 1, 3, 10, 20, 30)), default = "default")
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeJson(
                """
                {
                  "facet": {
                    "facets": {
                      "name": {
                        "type": "date",
                        "path": "path",
                        "boundaries": [
                          {
                            "${"$"}date": "2023-01-03T10:20:30Z"
                          }
                        ],
                        "default": "default"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
