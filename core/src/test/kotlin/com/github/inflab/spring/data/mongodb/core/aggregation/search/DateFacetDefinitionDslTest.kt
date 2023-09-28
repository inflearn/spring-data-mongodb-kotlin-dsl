package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate
import java.time.LocalDateTime

internal class DateFacetDefinitionDslTest : FreeSpec({
    fun dateFacet(block: DateFacetDefinitionDsl.() -> Unit) =
        DateFacetDefinitionDsl().apply(block)

    "boundaries" - {
        "should build a boundaries with LocalDate" {
            // given
            val definition = dateFacet {
                boundaries(
                    LocalDate.of(2021, 1, 1),
                    LocalDate.of(2021, 2, 1),
                )
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "date",
                  "boundaries": [
                    {
                      "${"$"}date": "2021-01-01T00:00:00Z"
                    },
                    {
                      "${"$"}date": "2021-02-01T00:00:00Z"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build a boundaries with LocalDateTime" {
            // given
            val definition = dateFacet {
                boundaries(
                    LocalDateTime.of(2021, 1, 1, 1, 20, 30),
                    LocalDateTime.of(2021, 2, 1, 2, 40, 50),
                )
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "date",
                  "boundaries": [
                    {
                      "${"$"}date": "2021-01-01T01:20:30Z"
                    },
                    {
                      "${"$"}date": "2021-02-01T02:40:50Z"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path by string value" {
            // given
            val definition = dateFacet {
                path("path")
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "date",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }

        "should build a path by property" {
            // given
            data class Test(val path: LocalDateTime)
            val definition = dateFacet {
                path(Test::path)
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "date",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }
    }

    "default" - {
        "should build a default" {
            // given
            val definition = dateFacet {
                default("default")
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "date",
                  "default": "default"
                }
                """.trimIndent(),
            )
        }
    }
})
