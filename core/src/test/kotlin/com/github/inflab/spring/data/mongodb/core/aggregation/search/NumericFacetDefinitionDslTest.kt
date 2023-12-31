package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class NumericFacetDefinitionDslTest : FreeSpec({
    fun numericFacet(block: NumericFacetDefinitionDsl.() -> Unit) =
        NumericFacetDefinitionDsl().apply(block)

    "boundaries" - {
        "should build a boundaries" {
            // given
            val definition = numericFacet {
                boundaries(
                    1,
                    2,
                )
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "number",
                  "boundaries": [
                    1,
                    2
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path by string value" {
            // given
            val definition = numericFacet {
                path("path")
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "number",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }

        "should build a path by property" {
            // given
            data class Test(val path: Long)
            val definition = numericFacet {
                path(Test::path)
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "number",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }
    }

    "default" - {
        "should build a default" {
            // given
            val definition = numericFacet {
                default("default")
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "number",
                  "default": "default"
                }
                """.trimIndent(),
            )
        }
    }
})
