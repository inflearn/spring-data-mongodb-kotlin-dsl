package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class AbsExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val absExpressionRepository = AbsExpressionRepository(mongoTemplate)

    beforeSpec {
        val changes = listOf(
            AbsExpressionRepository.TemperatureChange(id = 1, startTemp = 50, endTemp = 80),
            AbsExpressionRepository.TemperatureChange(id = 2, startTemp = 40, endTemp = 40),
            AbsExpressionRepository.TemperatureChange(id = 3, startTemp = 90, endTemp = 70),
            AbsExpressionRepository.TemperatureChange(id = 4, startTemp = 60, endTemp = 70),
        )
        mongoTemplate.insertAll(changes)
    }

    "absDelta" {
        // when
        val result = absExpressionRepository.absDelta()

        // then
        result.mappedResults.map { it.delta } shouldBe listOf(
            30,
            0,
            20,
            10,
        )
    }
})
