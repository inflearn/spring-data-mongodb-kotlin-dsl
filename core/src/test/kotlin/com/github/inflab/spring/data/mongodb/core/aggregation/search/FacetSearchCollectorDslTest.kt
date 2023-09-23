package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate

internal class FacetSearchCollectorDslTest : FreeSpec({
    fun facet(block: FacetSearchCollectorDsl.() -> Unit) =
        FacetSearchCollectorDsl().apply(block)

    "operator" - {
        "should set operator" {
            // given
            val collector = facet {
                operator {
                    text {
                        path("fieldName")
                    }
                }
            }

            // when
            val result = collector.build()

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
        "should set string facet" {
            // given
            val collector = facet {
                "name" stringFacet {
                    path("path")
                }
            }

            // when
            val result = collector.build()

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
    }

    "numericFacet" - {
        "should set numeric facet" {
            // given
            val collector = facet {
                "name" numericFacet {
                    path("path")
                    boundaries(1, 2, 3)
                }
            }

            // when
            val result = collector.build()

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
    }

    "dateFacet" - {
        "should set date facet" {
            // given
            val collector = facet {
                "name" dateFacet {
                    path("path")
                    boundaries(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 2, 1))
                }
            }

            // when
            val result = collector.build()

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
    }
})
