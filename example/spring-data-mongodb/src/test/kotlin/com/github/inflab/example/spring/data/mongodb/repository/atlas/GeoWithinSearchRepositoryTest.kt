package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_airbnb")
internal class GeoWithinSearchRepositoryTest(
    private val geoWithinSearchRepository: GeoWithinSearchRepository,
) : FreeSpec({

    "findByBox" {
        // when
        val result = geoWithinSearchRepository.findByBox()

        // then
        result.mappedResults.map { it.name } shouldBe listOf(
            "Surry Hills Studio - Your Perfect Base in Sydney",
            "Sydney Hyde Park City Apartment (checkin from 6am)",
            "THE Place to See Sydney's FIREWORKS",
        )
        result.mappedResults.map { it.address.location.coordinates } shouldBe listOf(
            listOf(151.21554, -33.88029),
            listOf(151.21346, -33.87603),
            listOf(151.17956, -33.86296),
        )
    }

    "findByCircle" {
        // when
        val result = geoWithinSearchRepository.findByCircle()

        // then
        result.mappedResults.map { it.name } shouldBe listOf(
            "Ligne verte - à 15 min de métro du centre ville.",
            "Belle chambre à côté Metro Papineau",
            "L'IDÉAL, ( à 2 min du métro Pie-IX ).",
        )
        result.mappedResults.map { it.address.location.coordinates } shouldBe listOf(
            listOf(-73.54949, 45.54548),
            listOf(-73.54985, 45.52797),
            listOf(-73.55208, 45.55157),
        )
    }

    "findByPolygon" {
        // when
        val result = geoWithinSearchRepository.findByPolygon()

        // then
        result.mappedResults.map { it.name } shouldBe listOf(
            "Ocean View Waikiki Marina w/prkg",
            "Kailua-Kona, Kona Coast II 2b condo",
            "LAHAINA, MAUI! RESORT/CONDO BEACHFRONT!! SLEEPS 4!",
        )
        result.mappedResults.map { it.address.location.coordinates } shouldBe listOf(
            listOf(-157.83919, 21.28634),
            listOf(-155.96445, 19.5702),
            listOf(-156.68012, 20.96996),
        )
    }

    "findByMultiPolygon" {
        // when
        val result = geoWithinSearchRepository.findByMultiPolygon()

        // then
        result.mappedResults.map { it.name } shouldBe listOf(
            "Heart of Honolulu, 2BD gem! Free Garage Parking!",
            "Private Studio closed to town w/ compact parking",
            "Comfortable Room (2) at Affordable Rates",
        )
        result.mappedResults.map { it.address.location.coordinates } shouldBe listOf(
            listOf(-157.84343, 21.30852),
            listOf(-157.85228, 21.31184),
            listOf(-157.83889, 21.29776),
        )
    }
})
