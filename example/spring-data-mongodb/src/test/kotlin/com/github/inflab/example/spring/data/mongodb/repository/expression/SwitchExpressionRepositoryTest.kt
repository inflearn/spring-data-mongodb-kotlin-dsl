package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class SwitchExpressionRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val switchExpressionRepository = SwitchExpressionRepository(mongoTemplate)

    beforeSpec {
        val grades = listOf(
            SwitchExpressionRepository.Grade(
                id = 1,
                name = "Susan Wilkes",
                scores = listOf(87, 86, 78),
            ),
            SwitchExpressionRepository.Grade(
                id = 2,
                name = "Bob Hanna",
                scores = listOf(71, 64, 81),
            ),
            SwitchExpressionRepository.Grade(
                id = 3,
                name = "James Torrelio",
                scores = listOf(91, 84, 97),
            ),
        )

        mongoTemplate.insertAll(grades)
    }

    "findSummary" {
        // when
        val result = switchExpressionRepository.findSummary()

        // then
        result.mappedResults shouldBe listOf(
            SwitchExpressionRepository.SummaryDto(
                id = 1,
                name = "Susan Wilkes",
                summary = "Doing pretty well.",
            ),
            SwitchExpressionRepository.SummaryDto(
                id = 2,
                name = "Bob Hanna",
                summary = "Needs improvement.",
            ),
            SwitchExpressionRepository.SummaryDto(
                id = 3,
                name = "James Torrelio",
                summary = "Doing great!",
            ),
        )
    }
})
