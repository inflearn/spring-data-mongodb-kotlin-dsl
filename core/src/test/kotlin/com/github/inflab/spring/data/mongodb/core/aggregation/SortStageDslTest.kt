package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec

internal class SortStageDslTest : FreeSpec({
    fun sort(block: SortStageDsl.() -> Unit) =
        SortStageDsl().apply(block)

    "ascending" - {
        "should add a field by string" {
            // given
            val stage = sort {
                "field" by Ascending
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "field": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            data class Child(val field: String)
            data class Parent(val child: Child, val parentField: String)
            val stage = sort {
                Parent::child..Child::field by Ascending
                Parent::parentField by Ascending
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "child.field": 1,
                    "parentField": 1
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "descending" - {
        "should add a field by string" {
            // given
            val stage = sort {
                "field" by Descending
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "field": -1
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            data class Child(val field: String)
            data class Parent(val child: Child, val parentField: String)
            val stage = sort {
                Parent::child..Child::field by Descending
                Parent::parentField by Descending
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "child.field": -1,
                    "parentField": -1
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "textScore" - {
        "should add a field by string" {
            // given
            val stage = sort {
                "field" by TextScore
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "field": {
                      "${'$'}meta": "textScore"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a field by property" {
            // given
            data class Child(val field: String)
            data class Parent(val child: Child, val parentField: String)
            val stage = sort {
                Parent::child..Child::field by TextScore
                Parent::parentField by TextScore
            }

            // when
            val result = stage.get()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}sort": {
                    "child.field": {
                      "${'$'}meta": "textScore"
                    },
                    "parentField": {
                      "${'$'}meta": "textScore"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
