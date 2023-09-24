package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import java.util.Date

@Ignored
@AtlasTest(database = "sample_mflix")
internal class FacetSearchRepositoryTest(
    private val facetSearchRepository: FacetSearchRepository,
) : FreeSpec({

    "findYearWithStringFacet" {
        // when
        val result = facetSearchRepository.findYearWithStringFacet()

        // then
        result.mappedResults.first().apply {
            count.lowerBound.shouldBeGreaterThan(10000)
            facet.genresFacet.buckets.map { it.id } shouldBe listOf(
                "Drama",
                "Comedy",
                "Romance",
                "Thriller",
                "Documentary",
                "Action",
                "Crime",
                "Adventure",
                "Horror",
                "Biography",
            )
            facet.genresFacet.buckets.map { it.count }.shouldBeMonotonicallyDecreasing()
        }
    }

    "findYearWithNumberFacet" {
        // when
        val result = facetSearchRepository.findYearWithNumberFacet()

        // then
        result.mappedResults.first().apply {
            count.lowerBound.shouldBeGreaterThan(6000)
            facet.yearFacet.buckets.map { it.id } shouldBe listOf(
                "1980",
                "1990",
                "other",
            )
            facet.yearFacet.buckets.map { it.count }.forAll {
                it.shouldBeGreaterThan(500)
            }
        }
    }

    "findReleasedWithDateFacet" {
        // when
        val result = facetSearchRepository.findReleasedWithDateFacet()

        // then
        result.mappedResults.first().apply {
            count.lowerBound.shouldBeGreaterThan(11000)
            facet.releaseFacet.buckets.map { it.id } shouldBe listOf(
                Date("2000/01/01 09:00:00"),
                Date("2005/01/01 09:00:00"),
                Date("2010/01/01 09:00:00"),
                "other",
            )
            facet.releaseFacet.buckets.map { it.count }.forAll {
                it.shouldBeGreaterThan(100)
            }
        }
    }
})
