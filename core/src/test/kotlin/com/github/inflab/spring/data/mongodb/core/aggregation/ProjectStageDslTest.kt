package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class ProjectStageDslTest : FreeSpec({
    data class Child(val field: String)
    data class Parent(val child: Child, val parentField: String)

    fun project(block: ProjectStageDsl.() -> Unit) =
        ProjectStageDsl().apply(block)

    "unaryPlus" - {
        "should add a field by string" {
            // given
            val stage = project {
                +"field"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "field": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            val stage = project {
                +(Parent::child..Child::field)
                +Parent::parentField
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "field": "${'$'}child.field",
                    "parentField": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a nested property" {
            // given
            val stage = project {
                +(Parent::child..Child::field)
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "field": "${'$'}child.field"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "unaryMinus" - {
        "should exclude a field by string" {
            // given
            val stage = project {
                -"field"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "field": 0
                  }
                }
                """.trimIndent(),
            )
        }

        "should exclude a field by property" {
            // given
            val stage = project {
                -(Parent::child..Child::field)
                -Parent::parentField
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "field": 0,
                    "parentField": 0
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "excludeId" - {
        "should exclude _id field" {
            // given
            val stage = project {
                excludeId()
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "_id": 0
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "alias" - {
        "should add a field with alias by string" {
            // given
            val stage = project {
                "field" alias "alias"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": "${'$'}field"
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field with alias by property" {
            // given
            val stage = project {
                (Parent::child..Child::field) alias "alias"
                Parent::parentField alias "alias2"
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": "${'$'}child.field",
                    "alias2": "${'$'}parentField"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "searchScore" - {
        "should add search score meta with given alias" {
            // given
            val stage = project {
                searchScore("alias")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": {
                      "${'$'}meta": "searchScore"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "searchScoreDetails" - {
        "should add search score details meta with given alias" {
            // given
            val stage = project {
                searchScoreDetails("alias")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": {
                      "${'$'}meta": "searchScoreDetails"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "searchHighlights" - {
        "should add search highlights meta with given alias" {
            // given
            val stage = project {
                searchHighlights("alias")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": {
                      "${'$'}meta": "searchHighlights"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "textScore" - {
        "should add text score meta with given alias" {
            // given
            val stage = project {
                textScore("alias")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": {
                      "${'$'}meta": "textScore"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "indexKey" - {
        "should add index key meta with given alias" {
            // given
            val stage = project {
                indexKey("alias")
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}project": {
                    "alias": {
                      "${'$'}meta": "indexKey"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
