package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class EqExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val eqExpressionRepository = EqExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            EqExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            EqExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            EqExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            EqExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            EqExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "qtyEq250" {
        // when
        val result = eqExpressionRepository.qtyEq250()

        // then
        result.mappedResults shouldBe listOf(
            EqExpressionRepository.EqDto("abc1", 300, false),
            EqExpressionRepository.EqDto("abc2", 200, false),
            EqExpressionRepository.EqDto("xyz1", 250, true),
            EqExpressionRepository.EqDto("VWZ1", 300, false),
            EqExpressionRepository.EqDto("VWZ2", 180, false),
        )
    }
})
