package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class IfNullExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val ifNullExpressionRepository = IfNullExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            IfNullExpressionRepository.Inventory(
                id = 1,
                item = "buggy",
                description = "toy car",
                quantity = 300,
            ),
            IfNullExpressionRepository.Inventory(
                id = 2,
                item = "bicycle",
                description = null,
                quantity = 200,
            ),
            IfNullExpressionRepository.Inventory(
                id = 3,
                item = "flag",
                description = null,
                quantity = null,
            ),
        )
        mongoTemplate.insertAll(inventories)
    }

    "findDescription" {
        // when
        val result = ifNullExpressionRepository.findDescription()

        // then
        result.mappedResults.map { it.description } shouldBe listOf(
            "toy car",
            "Unspecified",
            "Unspecified",
        )
    }

    "findValue" {
        // when
        val result = ifNullExpressionRepository.findValue()

        // then
        result.mappedResults.map { it.value } shouldBe listOf(
            "toy car",
            200,
            "Unspecified",
        )
    }
})
