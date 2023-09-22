package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.bson.types.ObjectId
import java.time.LocalDate
import java.time.LocalDateTime

internal class EqualsSearchOperatorDslTest : FreeSpec({
    fun equal(block: EqualsSearchOperatorDsl.() -> Unit): EqualsSearchOperatorDsl =
        EqualsSearchOperatorDsl().apply(block)

    "path" - {
        "should set path by strings" {
            // given
            val operator = equal {
                path("path")
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "path": "path"
                  }
                }
                """.trimIndent(),
            )
        }

        "should set path by properties" {
            // given
            data class Test(val property: String)
            val operator = equal {
                path(Test::property)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "path": "property"
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "value" - {
        listOf(true, false).forEach {
            "should set value by boolean $it" {
                // given
                val operator = equal {
                    value(it)
                }

                // when
                val result = operator.build()

                // then
                result.shouldBeJson(
                    """
                {
                  "equals": {
                    "value": $it
                  }
                }
                    """.trimIndent(),
                )
            }
        }

        "should set value by LocalDateTime" {
            // given
            val operator = equal {
                value(LocalDateTime.of(2023, 9, 18, 4, 10, 50, 1))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "value": {
                      "${'$'}date": "2023-09-18T04:10:50Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set value by LocalDate" {
            // given
            val operator = equal {
                value(LocalDate.of(2023, 9, 18))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "value": {
                      "${'$'}date": "2023-09-18T00:00:00Z"
                    }
                  }
                }
                """.trimIndent(),
            )
        }

        "should set value by number" {
            // given
            val operator = equal {
                value(1)
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "value": 1
                  }
                }
                """.trimIndent(),
            )
        }

        "should set value by ObjectId" {
            // given
            val operator = equal {
                value(ObjectId("6145f2b3d9b9f3b3e8f1b1a1"))
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "value": {
                      "${'$'}oid": "6145f2b3d9b9f3b3e8f1b1a1"
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }

    "score" - {
        "should set score" {
            // given
            val operator = equal {
                score {
                    boost(5.0)
                }
            }

            // when
            val result = operator.build()

            // then
            result.shouldBeJson(
                """
                {
                  "equals": {
                    "score": {
                      "boost": {
                        "value": 5.0
                      }
                    }
                  }
                }
                """.trimIndent(),
            )
        }
    }
})
