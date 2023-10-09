package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class SubtractExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val subtractExpressionRepository = SubtractExpressionRepository(mongoTemplate)

    beforeSpec {
        val sales = listOf(
            SubtractExpressionRepository.Sales(
                id = 1,
                item = "abc",
                price = 10,
                fee = 2,
                discount = 5,
                date = LocalDateTime.of(2014, 3, 1, 8, 0, 0),
            ),
            SubtractExpressionRepository.Sales(
                id = 2,
                item = "jkl",
                price = 20,
                fee = 1,
                discount = 2,
                date = LocalDateTime.of(2014, 3, 1, 9, 0, 0),
            ),
        )
        mongoTemplate.insertAll(sales)
    }

    "subtractNumber" {
        // when
        val result = subtractExpressionRepository.subtractNumber()

        // then
        result.mappedResults.map { it.total } shouldBe listOf(7, 19)
    }

    "subtractTwoDates" {
        // when
        val now = System.currentTimeMillis()
        val result = subtractExpressionRepository.subtractTwoDates()

        // then
        result.mappedResults.forAll {
            it.dateDifference shouldBeLessThan now
        }
    }

    "subtractMilliseconds" {
        // when
        val result = subtractExpressionRepository.subtractMilliseconds()

        // then
        result.mappedResults.map { it.dateDifference } shouldBe listOf(
            LocalDateTime.of(2014, 3, 1, 7, 55, 0),
            LocalDateTime.of(2014, 3, 1, 8, 55, 0),
        )
    }
})
