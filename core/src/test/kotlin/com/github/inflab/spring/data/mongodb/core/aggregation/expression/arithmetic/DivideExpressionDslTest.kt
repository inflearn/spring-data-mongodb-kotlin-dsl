package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators.Divide

internal class DivideExpressionDslTest : FreeSpec({

    "of" - {
        "should create a dividend by string" {
            // given
            val field = "field"

            // when
            val result = DivideExpressionDsl().of(field)

            // then
            result.value.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    "$$field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a dividend by property" {
            // given
            data class Test(val field: Long?)

            // when
            val result = DivideExpressionDsl().of(Test::field)

            // then
            result.value.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    "${'$'}field"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should create a dividend by number" {
            // given
            val number = 100

            // when
            val result = DivideExpressionDsl().of(number)

            // then
            result.value.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    $number
                  ]
                }
                """.trimIndent(),
            )
        }
    }

    "by" - {
        "should build a divide by string" {
            // given
            val dividend = DivideExpressionDsl().of(100)
            val block: DivideExpressionDsl.() -> Divide = { dividend by "divisor" }

            // when
            val result = DivideExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    100,
                    "${'$'}divisor"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build a divide by property" {
            // given
            val dividend = DivideExpressionDsl().of(100)
            data class Test(val divisor: Long?)
            val block: DivideExpressionDsl.() -> Divide = { dividend by Test::divisor }

            // when
            val result = DivideExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    100,
                    "${'$'}divisor"
                  ]
                }
                """.trimIndent(),
            )
        }

        "should build a divide by number" {
            // given
            val dividend = DivideExpressionDsl().of(100)
            val block: DivideExpressionDsl.() -> Divide = { dividend by 10 }

            // when
            val result = DivideExpressionDsl().block()

            // then
            result.shouldBeJson(
                """
                {
                  "${'$'}divide": [
                    100,
                    10
                  ]
                }
                """.trimIndent(),
            )
        }
    }
})
