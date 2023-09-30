package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample")
internal class ExistsSearchRepositoryTest(
    private val existsSearchRepository: ExistsSearchRepository,
) : FreeSpec({

    "findAppleByCompound" {
        // when
        val result = existsSearchRepository.findAppleByCompound()

        // then
        result.mappedResults.map { it.id } shouldBe listOf("1", "3")
    }
})
