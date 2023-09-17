package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import java.time.LocalDate
import java.time.LocalDateTime

class DateFacetDefinitionDslTest : FreeSpec({
    fun dateFacet(block: DateFacetDefinitionDsl.() -> Unit) =
        DateFacetDefinitionDsl().apply(block)

    "boundaries" - {
        "should set boundaries with LocalDate" {
            // given
            val stage = dateFacet {
                boundaries(
                    LocalDate.of(2021, 1, 1),
                    LocalDate.of(2021, 2, 1),
                )
            }

            // when
            val result = stage.get()

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

        "should set boundaries with LocalDateTime" {
            // given
            val stage = dateFacet {
                boundaries(
                    LocalDateTime.of(2021, 1, 1, 1, 20, 30),
                    LocalDateTime.of(2021, 2, 1, 2, 40, 50),
                )
            }

            // when
            val result = stage.get()

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
        "should set path by string value" {
            // given
            val stage = dateFacet {
                path("path")
            }

            // when
            val result = stage.get()

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

        "should set path by property" {
            // given
            data class Test(val path: LocalDateTime)
            val stage = dateFacet {
                path(Test::path)
            }

            // when
            val result = stage.get()

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
        "should set default" {
            // given
            val stage = dateFacet {
                default("default")
            }

            // when
            val result = stage.get()

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
