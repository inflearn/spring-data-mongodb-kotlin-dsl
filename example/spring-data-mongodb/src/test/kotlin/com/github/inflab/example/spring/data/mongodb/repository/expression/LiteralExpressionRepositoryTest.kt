package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LiteralExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val literalExpressionRepository = LiteralExpressionRepository(mongoTemplate)

    beforeSpec {
        val inventories = listOf(
            LiteralExpressionRepository.StoreInventory(
                id = 1,
                item = "napkins",
                price = "$2.50",
            ),
            LiteralExpressionRepository.StoreInventory(
                id = 2,
                item = "coffee",
                price = "1",
            ),
            LiteralExpressionRepository.StoreInventory(
                id = 3,
                item = "soap",
                price = "$1",
            ),
        )
        mongoTemplate.insertAll(inventories)

        val books = listOf(
            LiteralExpressionRepository.Book(
                id = 1,
                title = "Dracula",
                condition = "new",
            ),
            LiteralExpressionRepository.Book(
                id = 2,
                title = "The Little Prince",
                condition = "new",
            ),
        )

        mongoTemplate.insertAll(books)
    }

    "findCostsOneDollar" {
        // when
        val result = literalExpressionRepository.findCostsOneDollar()

        // then
        result.mappedResults shouldBe listOf(
            LiteralExpressionRepository.CostsOneDollarDto(id = 1, costsOneDollar = false),
            LiteralExpressionRepository.CostsOneDollarDto(id = 2, costsOneDollar = false),
            LiteralExpressionRepository.CostsOneDollarDto(id = 3, costsOneDollar = true),
        )
    }

    "addEditionNumberValue" {
        // when
        val result = literalExpressionRepository.addEditionNumberValue()

        // then
        result.mappedResults shouldBe listOf(
            LiteralExpressionRepository.EditionNumberValueDto(id = 1, title = "Dracula", editionNumber = 1),
            LiteralExpressionRepository.EditionNumberValueDto(id = 2, title = "The Little Prince", editionNumber = 1),
        )
    }
})
