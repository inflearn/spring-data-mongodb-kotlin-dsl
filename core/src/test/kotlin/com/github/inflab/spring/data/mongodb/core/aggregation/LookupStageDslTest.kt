package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.StringOperators
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let
import org.springframework.data.mongodb.core.mapping.Document

internal class LookupStageDslTest : FreeSpec({
    fun lookup(block: LookupStageDsl.() -> Unit) =
        LookupStageDsl().apply(block)

    "from" - {
        "should build a from by string" {
            // given
            val stage = lookup {
                from("collection")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "from": "collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a from by class" {
            // given
            data class Collection(val property: String)

            val stage = lookup {
                from(Collection::class)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "from": "Collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should use collection name from @Document annotation's collection value" {
            // given
            @Document(collection = "test_collection")
            data class Collection(val property: String)

            val stage = lookup {
                from(Collection::class)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "from": "test_collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should use collection name from @Document annotation's value" {
            // given
            @Document("test_collection")
            data class Collection(val property: String)

            val stage = lookup {
                from(Collection::class)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "from": "test_collection"
                  }
                }
                """.trimIndent(),
            )
        }

        "should use class name if @Document annotation's properties are empty" {
            // given
            @Document(collection = "", value = "")
            data class Collection(val property: String)

            val stage = lookup {
                from(Collection::class)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "from": "Collection"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "localField" - {
        "should build a localField by string" {
            // given
            val stage = lookup {
                localField("localField")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "localField": "localField"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a localField by property" {
            // given
            data class Collection(val localField: String)

            val stage = lookup {
                localField(Collection::localField)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "localField": "localField"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "foreignField" - {
        "should build a foreignField by string" {
            // given
            val stage = lookup {
                foreignField("foreignField")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "foreignField": "foreignField"
                  }
                }
                """.trimIndent(),
            )
        }

        "should build a foreignField by property" {
            // given
            data class Collection(val foreignField: String)

            val stage = lookup {
                foreignField(Collection::foreignField)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "foreignField": "foreignField"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "pipeline" - {
        "should build a pipeline" {
            // given
            val stage = lookup {
                pipeline {
                    count("test")
                }
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "pipeline": [
                      {
                        "${'$'}count": "test"
                      }
                    ]
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "let" - {
        "should build a let option" {
            // given
            val stage = lookup {
                let(
                    Let.define(
                        Let.ExpressionVariable.newVariable("order").forExpression(
                            org.bson.Document("\$reverseArray", "\$order"),
                        ),
                    ).andApply(StringOperators.valueOf("test").ltrim()),
                )
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "let": {
                      "order": {
                        "${'$'}reverseArray": "${'$'}order"
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "as" - {
        "should build a as option" {
            // given
            val stage = lookup {
                `as`("as")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}lookup": {
                    "as": "as"
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
