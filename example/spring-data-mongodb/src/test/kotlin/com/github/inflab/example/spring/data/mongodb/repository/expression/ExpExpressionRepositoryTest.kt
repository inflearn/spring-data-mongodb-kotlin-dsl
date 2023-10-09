package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class ExpExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val expExpressionRepository = ExpExpressionRepository(mongoTemplate)

    beforeSpec {
        val accounts = listOf(
            ExpExpressionRepository.Accounts(
                id = 1,
                interestRate = .08,
                presentValue = 10000,
            ),
            ExpExpressionRepository.Accounts(
                id = 2,
                interestRate = .0825,
                presentValue = 250000,
            ),
            ExpExpressionRepository.Accounts(
                id = 3,
                interestRate = .0425,
                presentValue = 1000,
            ),
        )
        mongoTemplate.insertAll(accounts)
    }

    "expRate" {
        // when
        val result = expExpressionRepository.expRate()

        // then
        result.mappedResults.map { it.effectiveRate } shouldBe listOf(
            0.08328706767495864,
            0.08599867343905654,
            0.04341605637367807,
        )
    }
})
