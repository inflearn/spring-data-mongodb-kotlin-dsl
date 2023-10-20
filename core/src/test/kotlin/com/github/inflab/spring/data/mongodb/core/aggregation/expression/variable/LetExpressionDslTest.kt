package com.github.inflab.spring.data.mongodb.core.aggregation.expression.variable

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let.ExpressionVariable

internal class LetExpressionDslTest : FreeSpec({
    fun let(block: LetExpressionDsl.() -> Unit) =
        LetExpressionDsl().apply(block).build()

    "variable" - {
        "should add a variable by name" {
            // given
            val field = "field"

            // when
            val expression = let {
                variable(field)
            }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}let": {
                    "vars": {
                      "field": null
                    },
                    "in": {}
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a variable by ExpressionVariable" {
            // given
            val field = "field"

            // when
            val expression = let {
                variable(ExpressionVariable.newVariable(field))
            }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}let": {
                    "vars": {
                      "field": null
                    },
                    "in": {}
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a variable by document" {
            // given
            val field = "field"
            val expressionObject = Document("\$add", listOf(1, 2))

            // when
            val expression = let {
                variable(field, expressionObject)
            }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}let": {
                    "vars": {
                      "field": {
                        "${'$'}add": [
                          1,
                          2
                        ]
                      }
                    },
                    "in": {}
                  }
                }
                """.trimIndent(),
            )
        }

        "should add a variable by AggregationExpression" {
            // given
            val field = "field"

            // when
            val expression = let {
                variable(field) { exp(2) }
            }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}let": {
                    "vars": {
                      "field": {
                        "${'$'}exp": 2
                      }
                    },
                    "in": {}
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "inExpression" - {
        "should add option" {
            // when
            val expression = let {
                inExpression { exp(1) }
            }

            // then
            expression.shouldBeJson(
                """
                {
                  "${'$'}let": {
                    "vars": {},
                    "in": {
                      "${'$'}exp": 1
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
