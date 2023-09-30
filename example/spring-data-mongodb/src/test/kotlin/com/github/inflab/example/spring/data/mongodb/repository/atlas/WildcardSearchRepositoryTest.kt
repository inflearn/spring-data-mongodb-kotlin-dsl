package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

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
        result.mappedResults.take(5).map { it.title } shouldBe listOf(
            "Where Are My Children?",
            "Who Killed Cock Robin?",
            "What's Opera, Doc?",
            "Will Success Spoil Rock Hunter?",
            "Who Was That Lady?",
        )
    }
})
