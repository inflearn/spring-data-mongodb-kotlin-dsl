package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.longs.shouldBeGreaterThan

@AtlasTest(database = "sample_mflix")
internal class SearchMetaRepositoryTest(
    private val searchMetaRepository: SearchMetaRepository,
) : FreeSpec({

    "findCountBetweenYear" {
        // when
        val result = searchMetaRepository.findCountBetweenYear()

        // then
        result.mappedResults.first().count.total.shouldBeGreaterThan(500)
    }
})
