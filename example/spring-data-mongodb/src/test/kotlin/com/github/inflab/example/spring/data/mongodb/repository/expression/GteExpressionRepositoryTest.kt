package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class GteExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val gteExpressionRepository = GteExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            GteExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            GteExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            GteExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            GteExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            GteExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyGte250" {
        // when
        val result = gteExpressionRepository.qtyGte250()

        // then
        result.mappedResults shouldBe listOf(
            GteExpressionRepository.GteDto(item = "abc1", qty = 300, qtyGte250 = true),
            GteExpressionRepository.GteDto(item = "abc2", qty = 200, qtyGte250 = false),
            GteExpressionRepository.GteDto(item = "xyz1", qty = 250, qtyGte250 = true),
            GteExpressionRepository.GteDto(item = "VWZ1", qty = 300, qtyGte250 = true),
            GteExpressionRepository.GteDto(item = "VWZ2", qty = 180, qtyGte250 = false),
        )
    }
})
