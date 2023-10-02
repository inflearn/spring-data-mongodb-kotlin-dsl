package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.LocalDate
import java.time.LocalDateTime

internal class NearSearchOperatorDslTest : FreeSpec({
    fun near(block: NearSearchOperatorDsl.() -> Unit): NearSearchOperatorDsl =
        NearSearchOperatorDsl().apply(block)

    "path" - {
        "should build a path by strings" {
            // given
            val operator = near {
                path("path1", "path2")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
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
            data class TestCollection(val path1: Number, val path2: Number)

            val operator = near {
                path(TestCollection::path1, TestCollection::path2)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
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
            data class Child(val path: Number)
            data class Parent(val child: Child)

            val operator = near {
                path(Parent::child..Child::path)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "path": "child.path"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "origin" - {
        "should build a origin by number" {
            // given
            val operator = near {
                origin(123)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "origin": 123
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a origin by LocalDateTime" {
            // given
            val operator = near {
                origin(LocalDateTime.of(2023, 9, 18, 4, 10, 50, 1))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "origin": {
                      "${'$'}date": "2023-09-18T04:10:50Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a origin by LocalDate" {
            // given
            val operator = near {
                origin(LocalDate.of(2023, 9, 18))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "origin": {
                      "${'$'}date": "2023-09-18T00:00:00Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a origin by GeoJson Point" {
            // given
            val operator = near {
                origin(GeoJsonPoint(1.0, 2.0))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "origin": {
                      "type": "Point",
                      "coordinates": [
                        1.0,
                        2.0
                      ]
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "pivot" - {
        "should build a pivot by Number" {
            // given
            val operator = near {
                pivot(123)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "pivot": 123
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should build a score" {
            // given
            val operator = near {
                score {
                    boost(5.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "near": {
                    "score": {
                      "boost": {
                        "value": 5.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
