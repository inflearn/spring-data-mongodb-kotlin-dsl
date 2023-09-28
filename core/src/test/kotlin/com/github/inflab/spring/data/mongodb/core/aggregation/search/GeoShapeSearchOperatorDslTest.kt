package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.geo.Point
import org.springframework.data.mongodb.core.geo.GeoJsonLineString
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon

internal class GeoShapeSearchOperatorDslTest : FreeSpec({
    fun geoShape(block: GeoShapeSearchOperatorDsl.() -> Unit) =
        GeoShapeSearchOperatorDsl().apply(block)

    "geometry" - {
        "should build a point" {
            // given
            val operator = geoShape {
                geometry(GeoJsonPoint(1.0, 0.0))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
                    "geometry": {
                      "type": "Point",
                      "coordinates": [
                        1.0,
                        0.0
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a line string" {
            // given
            val operator = geoShape {
                geometry(
                    GeoJsonLineString(
                        Point(0.0, 0.0),
                        Point(0.0, 1.0),
                    ),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
                    "geometry": {
                      "type": "LineString",
                      "coordinates": [
                        [
                          0.0,
                          0.0
                        ],
                        [
                          0.0,
                          1.0
                        ]
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a polygon" {
            // given
            val operator = geoShape {
                geometry(
                    GeoJsonPolygon(
                        Point(0.0, 0.0),
                        Point(0.0, 1.0),
                        Point(1.0, 1.0),
                        Point(1.0, 0.0),
                    ),
                )
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
                    "geometry": {
                      "type": "Polygon",
                      "coordinates": [
                        [
                          [
                            0.0,
                            0.0
                          ],
                          [
                            0.0,
                            1.0
                          ],
                          [
                            1.0,
                            1.0
                          ],
                          [
                            1.0,
                            0.0
                          ]
                        ]
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a multi polygon" {
            // given
            val operator = geoShape {
                geometry(
                    GeoJsonMultiPolygon(
                        listOf(
                            GeoJsonPolygon(
                                Point(0.0, 0.0),
                                Point(0.0, 1.0),
                                Point(1.0, 1.0),
                                Point(1.0, 0.0),
                            ),
                            GeoJsonPolygon(
                                Point(0.0, 0.0),
                                Point(0.0, 1.0),
                                Point(1.0, 1.0),
                                Point(1.0, 0.0),
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
                  "geoShape": {
                    "geometry": {
                      "type": "MultiPolygon",
                      "coordinates": [
                        [
                          [
                            [
                              0.0,
                              0.0
                            ],
                            [
                              0.0,
                              1.0
                            ],
                            [
                              1.0,
                              1.0
                            ],
                            [
                              1.0,
                              0.0
                            ]
                          ]
                        ],
                        [
                          [
                            [
                              0.0,
                              0.0
                            ],
                            [
                              0.0,
                              1.0
                            ],
                            [
                              1.0,
                              1.0
                            ],
                            [
                              1.0,
                              0.0
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
        "should build a path by strings" {
            // given
            val operator = geoShape {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
                    "path": [
                      "path1",
                      "path2"
                    ]
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by multiple properties" {
            // given
            data class TestCollection(val path1: GeoJsonPolygon, val path2: GeoJsonPolygon)
            val operator = geoShape {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
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
            data class Child(val path: GeoJsonLineString)
            data class Parent(val child: Child)
            val operator = geoShape {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "geoShape": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a path by option block" {
            // given
            data class TestCollection(val path1: GeoJsonPolygon, val path2: GeoJsonPoint)
            val operator = geoShape {
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
                  "geoShape": {
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

    "relation" - {
        GeoShapeRelation.entries.forEach {
            "should build a relation to $it" {
                // given
                val operator = geoShape {
                    relation = it
                }

                // when
                val result = operator.build()

                // then
                result.shouldBeJson(
                    """
                    {
                      "geoShape": {
                        "relation": "${it.name.lowercase()}"
                      }
                    }
                    """.trimIndent(),
                )
            }
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operator = geoShape {
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
                  "geoShape": {
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
