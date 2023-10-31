package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

@AtlasTest(database = "sample_analytics")
internal class InSearchRepositoryTest(
    private val inSearchRepository: InSearchRepository,
) : FreeSpec({

    "findBirthDate" {
        // when
        val result = inSearchRepository.findBirthDate()

        // then
        result.mappedResults.map { it.name } shouldBe listOf("Elizabeth Ray", "Brad Cardenas")
        result.mappedResults.map { it.birthdate } shouldBe listOf(
            LocalDateTime.of(1977, 3, 2, 11, 20, 31),
            LocalDateTime.of(1977, 5, 7, 6, 57, 35),
        )
    }

    "findAccountsByArray" {
        // when
        val result = inSearchRepository.findAccountsByArray()

        // then
        result.mappedResults.map { it.name } shouldBe listOf("Elizabeth Ray")
        result.mappedResults.map { it.accounts } shouldBe listOf(371138, 324287, 276528, 332179, 422649, 387979)
    }

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
