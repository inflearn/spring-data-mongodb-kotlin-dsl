package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LtExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val ltExpressionRepository = LtExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            LtExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            LtExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            LtExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            LtExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            LtExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyLt250" {
        // when
        val result = ltExpressionRepository.qtyLt250()

        // then
        result.mappedResults shouldBe listOf(
            LtExpressionRepository.LtDto(
                item = "abc1",
                qty = 300,
                qtyLt250 = false,
            ),
            LtExpressionRepository.LtDto(
                item = "abc2",
                qty = 200,
                qtyLt250 = true,
            ),
            LtExpressionRepository.LtDto(
                item = "xyz1",
                qty = 250,
                qtyLt250 = false,
            ),
            LtExpressionRepository.LtDto(
                item = "VWZ1",
                qty = 300,
                qtyLt250 = false,
            ),
            LtExpressionRepository.LtDto(
                item = "VWZ2",
                qty = 180,
                qtyLt250 = true,
            ),
        )
    }
})
