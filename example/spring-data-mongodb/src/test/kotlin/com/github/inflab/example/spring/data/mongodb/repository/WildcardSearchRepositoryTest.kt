package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

@Ignored
@AtlasTest(database = "sample_mflix")
internal class WildcardSearchRepositoryTest(
    private val wildcardSearchRepository: WildcardSearchRepository,
) : FreeSpec({

    "findTitleWithGreenD" {
        // when
        val result = wildcardSearchRepository.findTitleWithGreenD()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Green Dolphin Street",
            "Green Dragon",
        )
    }

    "findTitleWithQuestionMark" {
        // when
        val result = wildcardSearchRepository.findTitleWithQuestionMark()

        // then
        result.mappedResults.map { it.title shouldContain "?" }
    }
})
