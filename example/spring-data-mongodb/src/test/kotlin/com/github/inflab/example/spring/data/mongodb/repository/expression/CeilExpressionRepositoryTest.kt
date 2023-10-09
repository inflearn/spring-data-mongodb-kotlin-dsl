package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class CeilExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val ceilExpressionRepository = CeilExpressionRepository(mongoTemplate)

    beforeSpec {
        val changes = listOf(
            CeilExpressionRepository.Samples(id = 1, value = 9.25),
            CeilExpressionRepository.Samples(id = 2, value = 8.73),
            CeilExpressionRepository.Samples(id = 3, value = 4.32),
            CeilExpressionRepository.Samples(id = 4, value = -5.34),
        )
        mongoTemplate.insertAll(changes)
    }

    "ceilValue" {
        // when
        val result = ceilExpressionRepository.ceilValue()

        // then
        result.mappedResults.map { it.ceilingValue } shouldBe listOf(
            10,
            9,
            5,
            -5,
        )
    }
})
