package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class AddExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val addExpressionRepository = AddExpressionRepository(mongoTemplate)

    beforeSpec {
        val sales = listOf(
            AddExpressionRepository.Sales(
                id = 1,
                item = "abc",
                price = 10,
                fee = 2,
                date = LocalDateTime.of(2014, 3, 1, 8, 0, 0),
            ),
            AddExpressionRepository.Sales(
                id = 2,
                item = "jkl",
                price = 20,
                fee = 1,
                date = LocalDateTime.of(2014, 3, 1, 9, 0, 0),
            ),
            AddExpressionRepository.Sales(
                id = 3,
                item = "xyz",
                price = 5,
                fee = 0,
                date = LocalDateTime.of(2014, 3, 15, 9, 0, 0),
            ),
        )
        mongoTemplate.insertAll(sales)
    }

    "addNumbers" {
        // when
        val result = addExpressionRepository.addNumbers()

        // then
        result.mappedResults.map { it.total } shouldBe listOf(
            12,
            21,
            5,
        )
    }

    "addDate" {
        // when
        val result = addExpressionRepository.addDate()

        // then
        result.mappedResults.map { it.billingDate } shouldBe listOf(
            LocalDateTime.of(2014, 3, 4, 8, 0, 0),
            LocalDateTime.of(2014, 3, 4, 9, 0, 0),
            LocalDateTime.of(2014, 3, 18, 9, 0, 0),
        )
    }
})
