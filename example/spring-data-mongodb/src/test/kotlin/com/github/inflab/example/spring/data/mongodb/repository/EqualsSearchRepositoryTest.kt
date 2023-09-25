package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe

@Ignored
@AtlasTest(database = "sample")
internal class EqualsSearchRepositoryTest(
    private val equalsSearchRepository: EqualsSearchRepository,
) : FreeSpec({

    "findVerifiedUser" {
        // when
        val result = equalsSearchRepository.findVerifiedUser()

        // then
        result.mappedResults.map { it.name } shouldBe listOf("Jim Hall", "Ellen Smith")
        result.mappedResults.map { it.score }.forAll { it shouldBe 1.0 }
    }

    "findAccountCreated" {
        // when
        val result = equalsSearchRepository.findAccountCreated()

        // then
        result.mappedResults.map { it.name } shouldBe listOf("Ellen Smith")
        result.mappedResults.map { it.score }.forAll { it shouldBe 1.0 }
    }
})
