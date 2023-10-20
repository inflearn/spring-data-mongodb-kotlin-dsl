package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class LetExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val letExpressionRepository = LetExpressionRepository(mongoTemplate)

    beforeSpec {
        val accounts = listOf(
            LetExpressionRepository.Sales(
                id = 1,
                price = 10,
                tax = 0.50,
                applyDiscount = true,
            ),
            LetExpressionRepository.Sales(
                id = 2,
                price = 10,
                tax = 0.25,
                applyDiscount = false,
            ),
        )
        mongoTemplate.insertAll(accounts)
    }

    "findFinalTotal" {
        // when
        val result = letExpressionRepository.findFinalTotal()

        // then
        result.mappedResults.map { it.finalTotal } shouldBe listOf(
            9.450000000000001,
            10.25,
        )
    }
})
