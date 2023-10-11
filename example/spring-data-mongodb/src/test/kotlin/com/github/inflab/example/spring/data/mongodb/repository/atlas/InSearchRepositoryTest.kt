package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_analytics")
internal class InSearchRepositoryTest(
    private val inSearchRepository: InSearchRepository,
) : FreeSpec({

    "findScoreByCompound" {
        // when
        val result = inSearchRepository.findScoreByCompound()

        // then
        result.mappedResults.take(5).map { it.name } shouldBe listOf(
            "James Sanchez",
            "James Moore",
            "James Smith",
            "James Lopez",
            "James Jones",
        )
        result.mappedResults.map { it.score }.shouldBeMonotonicallyDecreasing()
    }
})
