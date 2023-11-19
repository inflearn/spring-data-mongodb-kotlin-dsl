package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class GtExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val gtExpressionRepository = GtExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            GtExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            GtExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            GtExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            GtExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            GtExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyGt250" {
        // when
        val result = gtExpressionRepository.qtyGt250()

        // then
        result.mappedResults shouldBe listOf(
            GtExpressionRepository.GtDto(
                item = "abc1",
                qty = 300,
                qtyGt250 = true,
            ),
            GtExpressionRepository.GtDto(
                item = "abc2",
                qty = 200,
                qtyGt250 = false,
            ),
            GtExpressionRepository.GtDto(
                item = "xyz1",
                qty = 250,
                qtyGt250 = false,
            ),
            GtExpressionRepository.GtDto(
                item = "VWZ1",
                qty = 300,
                qtyGt250 = true,
            ),
            GtExpressionRepository.GtDto(
                item = "VWZ2",
                qty = 180,
                qtyGt250 = false,
            ),
        )
    }
})
