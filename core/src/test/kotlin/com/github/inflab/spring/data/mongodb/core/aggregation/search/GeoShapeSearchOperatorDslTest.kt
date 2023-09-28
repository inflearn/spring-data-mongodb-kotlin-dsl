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
        "should set point" {
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

        "should set line string" {
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

        "should set polygon" {
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

        "should set multiy polygon" {
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
        "should set path by strings" {
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

        "should set path by multiple properties" {
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

        "should set path by nested property" {
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
    }

    "relation" - {
        GeoShapeRelation.entries.forEach {
            "should set relation to $it" {
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
        "should set score" {
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
