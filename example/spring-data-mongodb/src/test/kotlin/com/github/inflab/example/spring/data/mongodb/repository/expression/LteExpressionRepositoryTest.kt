package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LteExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val lteExpressionRepository = LteExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            LteExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            LteExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            LteExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            LteExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            LteExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyLte250" {
        // when
        val result = lteExpressionRepository.qtyLte250()

        // then
        result.mappedResults shouldBe listOf(
            LteExpressionRepository.LteDto(item = "abc1", qty = 300, qtyLte250 = false),
            LteExpressionRepository.LteDto(item = "abc2", qty = 200, qtyLte250 = true),
            LteExpressionRepository.LteDto(item = "xyz1", qty = 250, qtyLte250 = true),
            LteExpressionRepository.LteDto(item = "VWZ1", qty = 300, qtyLte250 = false),
            LteExpressionRepository.LteDto(item = "VWZ2", qty = 180, qtyLte250 = true),
        )
    }
})
