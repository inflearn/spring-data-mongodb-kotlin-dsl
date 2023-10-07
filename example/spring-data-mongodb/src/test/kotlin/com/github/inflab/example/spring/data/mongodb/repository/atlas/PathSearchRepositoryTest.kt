package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample")
internal class PathSearchRepositoryTest(
    private val pathSearchRepository: PathSearchRepository,
) : FreeSpec({

    "findBySingleField" {
        // when
        val result = pathSearchRepository.findBySingleField()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(3)
    }

    "findByMultipleField" {
        // when
        val result = pathSearchRepository.findByMultipleField()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(1)
    }

    "findBySimpleAnalyzer" {
        // when
        val result = pathSearchRepository.findBySimpleAnalyzer()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(2)
    }

    "findByWhitespaceAnalyzer" {
        // when
        val result = pathSearchRepository.findByWhitespaceAnalyzer()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(1)
    }

    "findByWildcard" {
        // when
        val result = pathSearchRepository.findByWildcard()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(1, 3)
    }

    "findByNestedWildcard" {
        // when
        val result = pathSearchRepository.findByNestedWildcard()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(1, 3)
    }
})
