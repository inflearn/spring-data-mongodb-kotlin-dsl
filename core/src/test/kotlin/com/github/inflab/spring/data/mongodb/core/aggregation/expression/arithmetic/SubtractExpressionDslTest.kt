package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import java.time.LocalDateTime

internal class SubtractExpressionDslTest : FreeSpec({

    "of" - {
        "should create a firstArg by string" {
            // given
            val firstArg = "firstArg"

            // when
            val result = SubtractExpressionDsl().of(firstArg)

            // then
            result shouldBe SubtractExpressionDsl.FirstArg(firstArg)
        }

        "should create a firstArg by date" {
            // given
            val firstArg = LocalDateTime.of(2021, 1, 1, 0, 0, 0)

            // when
            val result = SubtractExpressionDsl().of(firstArg)

            // then
            result shouldBe SubtractExpressionDsl.FirstArg(firstArg)
        }

        "should create a firstArg by date property" {
            // given
            data class Sample(val firstArg: LocalDateTime?)

            // when
            val result = SubtractExpressionDsl().of(Sample::firstArg)

            // then
            result shouldBe SubtractExpressionDsl.FirstArg("firstArg")
        }

        "should create a firstArg by number property" {
            // given
            data class Sample(val firstArg: Double?)

            // when
            val result = SubtractExpressionDsl().of(Sample::firstArg)

            // then
            result shouldBe SubtractExpressionDsl.FirstArg("firstArg")
        }
    }

    "minus" - {
        "should build an expression by string" {
            // given
            val firstArg = SubtractExpressionDsl.FirstArg("firstArg")
            val secondArg = "secondArg"
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - secondArg
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    "$${firstArg.value}",
                    "$$secondArg"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an expression by date" {
            // given
            val firstArg = SubtractExpressionDsl.FirstArg(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
            val secondArg = LocalDateTime.of(2020, 4, 5, 0, 0, 0)
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - secondArg
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    {
                      "${'$'}date": "2021-01-01T00:00:00Z"
                    },
                    {
                      "${'$'}date": "2020-04-05T00:00:00Z"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an expression by number" {
            // given
            val firstArg = SubtractExpressionDsl.FirstArg(100)
            val secondArg = 50
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - secondArg
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    100,
                    50
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an expression by expression" {
            // given
            val firstArg = SubtractExpressionDsl.FirstArg("firstArg")
            val secondArg = "secondArg"
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - { abs(secondArg) }
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    "$${firstArg.value}",
                    {
                      "${'$'}abs": "$$secondArg"
                    }
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an expression by number property" {
            // given
            data class Sample(val secondArg: Double?)
            val firstArg = SubtractExpressionDsl.FirstArg("firstArg")
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - Sample::secondArg
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    "$${firstArg.value}",
                    "$${Sample::secondArg.name}"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build an expression by date property" {
            // given
            data class Sample(val secondArg: LocalDateTime?)
            val firstArg = SubtractExpressionDsl.FirstArg("firstArg")
            val block: SubtractExpressionDsl.() -> AggregationExpression = {
                firstArg - Sample::secondArg
            }

            // when
            val result = SubtractExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}subtract": [
                    "$${firstArg.value}",
                    "$${Sample::secondArg.name}"
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
