package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@Ignored
@AtlasTest(database = "sample_airbnb")
internal class GeoShapeSearchRepositoryTest(
    private val geoShapeSearchRepository: GeoShapeSearchRepository,
) : FreeSpec({

    "findByDisjoint" {
        // when
        val result = geoShapeSearchRepository.findByDisjoint()

        // then
        result.mappedResults.take(3).map { it.name } shouldBe listOf(
            "Ribeira Charming Duplex",
            "Horto flat with small garden",
            "Private Room in Bushwick",
        )
        result.mappedResults.take(3).map { it.address.location.coordinates } shouldBe listOf(
            listOf(-8.61308, 41.1413),
            listOf(-43.23074991429229, -22.966253551739655),
            listOf(-73.93615, 40.69791),
        )
    }

    "findByIntersects" {
        // when
        val result = geoShapeSearchRepository.findByIntersects()

        // then
        result.mappedResults.take(3).map { it.name } shouldBe listOf(
            "Cozy bedroom Sagrada Familia",
            "",
            "SPACIOUS RAMBLA CATALUÑA",
        )
        result.mappedResults.take(3).map { it.address.location.coordinates } shouldBe listOf(
            listOf(2.17963, 41.40087),
            listOf(2.15759, 41.40349),
            listOf(2.15255, 41.39193),
        )
    }

    "findByWithin" {
        // when
        val result = geoShapeSearchRepository.findByWithin()

        // then
        result.mappedResults.take(3).map { it.name } shouldBe listOf(
            "Private Room in Bushwick",
            "New York City - Upper West Side Apt",
            "Deluxe Loft Suite",
        )
        result.mappedResults.take(3).map { it.address.location.coordinates } shouldBe listOf(
            listOf(-73.93615, 40.69791),
            listOf(-73.96523, 40.79962),
            listOf(-73.94472, 40.72778),
        )
    }
})
