package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class NeExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val neExpressionRepository = NeExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            NeExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            NeExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            NeExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            NeExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            NeExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyNe250" {
        // when
        val result = neExpressionRepository.qtyNe250()

        // then
        result.mappedResults shouldBe listOf(
            NeExpressionRepository.NeDto(item = "abc1", qty = 300, qtyNe250 = true),
            NeExpressionRepository.NeDto(item = "abc2", qty = 200, qtyNe250 = true),
            NeExpressionRepository.NeDto(item = "xyz1", qty = 250, qtyNe250 = false),
            NeExpressionRepository.NeDto(item = "VWZ1", qty = 300, qtyNe250 = true),
            NeExpressionRepository.NeDto(item = "VWZ2", qty = 180, qtyNe250 = true),
        )
    }
})
