package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class RegexSearchRepositoryTest(
    private val regexSearchRepository: RegexSearchRepository,
) : FreeSpec({

    "findTitleEndSeattle" {
        // when
        val result = regexSearchRepository.findEndSeattleTitle()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Sleepless in Seattle",
            "Battle in Seattle",
        )
    }

    "findRegexTitle" {
        // when
        val result = regexSearchRepository.findRegexTitle()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "20 Dates",
            "25 Watts",
            "21 Grams",
            "13 Lakes",
            "18 Meals",
            "26 Years",
            "99 Homes",
            "45 Years",
        )
    }
})
