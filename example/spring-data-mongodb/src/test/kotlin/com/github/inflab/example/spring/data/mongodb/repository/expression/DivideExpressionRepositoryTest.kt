package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class DivideExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val divideExpressionRepository = DivideExpressionRepository(mongoTemplate)

    beforeSpec {
        val plannings = listOf(
            DivideExpressionRepository.ConferencePlanning(
                id = 1,
                city = "New York",
                hours = 80,
                takes = 7,
            ),
            DivideExpressionRepository.ConferencePlanning(
                id = 2,
                city = "Singapore",
                hours = 40,
                takes = 4,
            ),
        )
        mongoTemplate.insertAll(plannings)
    }

    "divide" {
        // when
        val result = divideExpressionRepository.divide()

        // then
        result.mappedResults.map { it.workdays } shouldBe listOf(10, 5)
    }
})
