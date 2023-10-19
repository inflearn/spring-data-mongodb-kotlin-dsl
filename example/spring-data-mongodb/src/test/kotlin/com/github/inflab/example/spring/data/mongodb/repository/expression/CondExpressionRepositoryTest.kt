package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class CondExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val condExpressionRepository = CondExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            CondExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                qty = 300,
            ),
            CondExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                qty = 200,
            ),
            CondExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                qty = 250,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "findDiscount" {
        // when
        val result = condExpressionRepository.findDiscount()

        // then
        result.mappedResults shouldBe listOf(
            CondExpressionRepository.DiscountDto(id = 1, item = "abc1", discount = 30),
            CondExpressionRepository.DiscountDto(id = 2, item = "abc2", discount = 20),
            CondExpressionRepository.DiscountDto(id = 3, item = "xyz1", discount = 30),
        )
    }
})
