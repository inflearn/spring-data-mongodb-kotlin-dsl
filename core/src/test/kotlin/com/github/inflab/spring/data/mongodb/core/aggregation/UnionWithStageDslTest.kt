package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.data.mongodb.core.mapping.Document

internal class UnionWithStageDslTest : FreeSpec({
    fun unionWith(block: UnionWithStageDsl.() -> Unit) =
        UnionWithStageDsl().apply(block)

    "coll" - {
        "should add a collection by string" {
            // given
            val stage = unionWith {
                coll("collection")
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a collection by class" {
            // given
            data class Collection(val property: String)
            val stage = unionWith {
                coll(Collection::class)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "Collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a collection by class with @Document annotation" {
            // given
            @Document("collection")
            data class Collection(val property: String)
            val stage = unionWith {
                coll(Collection::class)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a collection by class with @Document annotation with value" {
            // given
            @Document(value = "collection")
            data class Collection(val property: String)
            val stage = unionWith {
                coll(Collection::class)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a collection by class with @Document annotation with collection" {
            // given
            @Document(collection = "collection")
            data class Collection(val property: String)
            val stage = unionWith {
                coll(Collection::class)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should use class name if @Document annotation's properties are empty" {
            // given
            @Document(collection = "", value = "")
            data class Collection(val property: String)

            val stage = unionWith {
                coll(Collection::class)
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "Collection"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "pipeline" - {
        "should not build stage if collection is not given" {
            // given
            val stage = unionWith {
                pipeline {
                    count("fieldName")
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldBeNull()
        }

        "should build stage if collection is given" {
            // given
            val stage = unionWith {
                coll("collection")
                pipeline {
                    count("fieldName")
                }
            }

            // when
            val result = stage.build()

            // then
            result.shouldNotBeNull()
            result.shouldBeJson(
                """
                {
                  "${'$'}unionWith": {
                    "coll": "collection",
                    "pipeline": [
                      {
                        "${'$'}count": "fieldName"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
