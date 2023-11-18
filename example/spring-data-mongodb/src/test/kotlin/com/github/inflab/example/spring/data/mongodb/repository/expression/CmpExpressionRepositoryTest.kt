package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class CmpExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val cmpExpressionRepository = CmpExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            CmpExpressionRepository.Inventory(
                id = 1,
                item = "abc1",
                description = "product 1",
                qty = 300,
            ),
            CmpExpressionRepository.Inventory(
                id = 2,
                item = "abc2",
                description = "product 2",
                qty = 200,
            ),
            CmpExpressionRepository.Inventory(
                id = 3,
                item = "xyz1",
                description = "product 3",
                qty = 250,
            ),
            CmpExpressionRepository.Inventory(
                id = 4,
                item = "VWZ1",
                description = "product 4",
                qty = 300,
            ),
            CmpExpressionRepository.Inventory(
                id = 5,
                item = "VWZ2",
                description = "product 5",
                qty = 180,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "compareTo250" {
        // when
        val result = cmpExpressionRepository.compareTo250()

        // then
        result.mappedResults shouldBe listOf(
            CmpExpressionRepository.CmpDto("abc1", 300, 1),
            CmpExpressionRepository.CmpDto("abc2", 200, -1),
            CmpExpressionRepository.CmpDto("xyz1", 250, 0),
            CmpExpressionRepository.CmpDto("VWZ1", 300, 1),
            CmpExpressionRepository.CmpDto("VWZ2", 180, -1),
        )
    }
})
