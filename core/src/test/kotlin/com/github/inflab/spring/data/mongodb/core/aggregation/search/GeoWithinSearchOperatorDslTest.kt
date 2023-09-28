package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon

internal class GeoWithinSearchOperatorDslTest : FreeSpec({
    fun geoWithin(block: GeoWithinSearchOperatorDsl.() -> Unit) =
        GeoWithinSearchOperatorDsl().apply(block)

    "box" - {
        "should build a box option" {
            // given
            val operator = geoWithin {
                box(bottomLeft = GeoJsonPoint(1.0, 2.0), topRight = GeoJsonPoint(3.0, 4.0))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
                    "box": {
                      "box": {
                        "bottomLeft": {
                          "type": "Point",
                          "coordinates": [
                            1.0,
                            2.0
                          ]
                        },
                        "topRight": {
                          "type": "Point",
                          "coordinates": [
                            3.0,
                            4.0
                          ]
                        }
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "circle" - {
        "should build a circle option" {
            // given
            val operator = geoWithin {
                circle(center = GeoJsonPoint(1.0, 2.0), radius = 3.0)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
                    "box": {
                      "circle": {
                        "center": {
                          "type": "Point",
                          "coordinates": [
                            1.0,
                            2.0
                          ]
                        },
                        "radius": 3.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "polygon" - {
        "should build a polygon option" {
            // given
            val operator = geoWithin {
                polygon(
                    GeoJsonPolygon(
                        GeoJsonPoint(1.0, 2.0),
                        GeoJsonPoint(3.0, 4.0),
                        GeoJsonPoint(5.0, 6.0),
                        GeoJsonPoint(1.0, 2.0),
                    ),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
                    "geometry": {
                      "type": "Polygon",
                      "coordinates": [
                        [
                          [
                            1.0,
                            2.0
                          ],
                          [
                            3.0,
                            4.0
                          ],
                          [
                            5.0,
                            6.0
                          ],
                          [
                            1.0,
                            2.0
                          ]
                        ]
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "multiPolygon" - {
        "should build a multi polygon option" {
            // given
            val operator = geoWithin {
                multiPolygon(
                    GeoJsonMultiPolygon(
                        listOf(
                            GeoJsonPolygon(
                                GeoJsonPoint(1.0, 2.0),
                                GeoJsonPoint(3.0, 4.0),
                                GeoJsonPoint(5.0, 6.0),
                                GeoJsonPoint(1.0, 2.0),
                            ),
                            GeoJsonPolygon(
                                GeoJsonPoint(1.0, 2.0),
                                GeoJsonPoint(3.0, 4.0),
                                GeoJsonPoint(5.0, 6.0),
                                GeoJsonPoint(1.0, 2.0),
                            ),
                        ),
                    ),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
                    "geometry": {
                      "type": "MultiPolygon",
                      "coordinates": [
                        [
                          [
                            [
                              1.0,
                              2.0
                            ],
                            [
                              3.0,
                              4.0
                            ],
                            [
                              5.0,
                              6.0
                            ],
                            [
                              1.0,
                              2.0
                            ]
                          ]
                        ],
                        [
                          [
                            [
                              1.0,
                              2.0
                            ],
                            [
                              3.0,
                              4.0
                            ],
                            [
                              5.0,
                              6.0
                            ],
                            [
                              1.0,
                              2.0
                            ]
                          ]
                        ]
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should set path by strings" {
            // given
            val operator = geoWithin {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
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
            data class TestCollection(val path1: GeoJsonPolygon, val path2: GeoJsonPolygon)
            val operator = geoWithin {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
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
            data class Child(val path: GeoJsonLineString)
            data class Parent(val child: Child)
            val operator = geoWithin {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoWithin": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by option block" {
            // given
            data class TestCollection(val path1: GeoJsonPolygon, val path2: GeoJsonPoint)
            val operator = geoWithin {
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
                  "geoWithin": {
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
        "should set score" {
            // given
            val operator = geoWithin {
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
                  "geoWithin": {
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
