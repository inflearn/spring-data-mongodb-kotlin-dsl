package com.github.inflab.spring.data.mongodb.core.aggregation.expression.conditional

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Cond
import org.springframework.data.mongodb.core.query.Criteria

internal class CondExpressionDslTest : FreeSpec({
    fun cond(block: CondExpressionDsl.() -> Cond) = CondExpressionDsl().block()

    "case" - {
        "should create a condition by document" {
            // given
            val document = Document("key", "value")

            // when
            val result = cond {
                case(document) then true otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": {
                      "key": "value"
                    },
                    "then": true,
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a condition by String" {
            // given
            val field = "path"

            // when
            val result = cond {
                case(field) then true otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "$$field",
                    "then": true,
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a condition by property" {
            // given
            data class Test(val property: Boolean?)

            // when
            val result = cond {
                case(Test::property) then true otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "${'$'}property",
                    "then": true,
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a condition by CriteriaDefinition" {
            // given
            val criteria = Criteria.where("fieldName")

            // when
            val result = cond {
                case(criteria) then true otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": [],
                    "then": true,
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a condition by expression" {
            // when
            val result = cond {
                case { abs("field") } then true otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": {
                      "${'$'}abs": "${'$'}field"
                    },
                    "then": true,
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "then" - {
        "should create a then by string field" {
            // when
            val result = cond {
                case("field") then "thenField" otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "${'$'}field",
                    "then": "${'$'}thenField",
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a then by expression" {
            // when
            val result = cond {
                case("field") then { literal(1) } otherwise false
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "${'$'}field",
                    "then": {
                      "${'$'}literal": 1
                    },
                    "else": false
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "otherwise" - {
        "should create a otherwise by string field" {
            // when
            val result = cond {
                case("field") then true otherwise "otherwiseField"
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "${'$'}field",
                    "then": true,
                    "else": "${'$'}otherwiseField"
                  }
                }
                """.trimIndent(),
            )
        }

        "should create a otherwise by expression" {
            // when
            val result = cond {
                case("field") then true otherwise { literal(1) }
            }

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}cond": {
                    "if": "${'$'}field",
                    "then": true,
                    "else": {
                      "${'$'}literal": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
