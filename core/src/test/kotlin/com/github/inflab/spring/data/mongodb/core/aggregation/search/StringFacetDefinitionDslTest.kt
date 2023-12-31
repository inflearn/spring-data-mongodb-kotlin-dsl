package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class StringFacetDefinitionDslTest : FreeSpec({
    fun stringFacet(block: StringFacetDefinitionDsl.() -> Unit) =
        StringFacetDefinitionDsl().apply(block)

    "boundaries" - {
        "should build a numBuckets" {
            // given
            val definition = stringFacet {
                numBuckets(10)
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "string",
                  "numBuckets": 10
                }
                """.trimIndent(),
            )
        }
    }

    "path" - {
        "should build a path by string value" {
            // given
            val definition = stringFacet {
                path("path")
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "string",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }

        "should build a path by property" {
            // given
            data class Test(val path: String)
            val definition = stringFacet {
                path(Test::path)
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "string",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }

        "should build a path by iterable property" {
            // given
            data class Test(val path: List<String>)
            val definition = stringFacet {
                path(Test::path)
            }

            // when
            val result = definition.get()

            // then
            result.shouldBeJson(
                """
                {
                  "type": "string",
                  "path": "path"
                }
                """.trimIndent(),
            )
        }
    }
})
