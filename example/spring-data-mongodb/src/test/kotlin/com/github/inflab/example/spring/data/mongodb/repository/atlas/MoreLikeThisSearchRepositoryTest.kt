package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_mflix")
internal class MoreLikeThisSearchRepositoryTest(
    private val moreLikeThisSearchRepository: MoreLikeThisSearchRepository,
) : FreeSpec({

    "findTitleAndGenres" {
        // when
        val result = moreLikeThisSearchRepository.findTitleAndGenres()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Godfather",
            "The Godfather",
            "The Godfather: Part II",
            "The Godfather: Part III",
            "The Defender",
        )
        result.mappedResults.map { it.genres } shouldBe listOf(
            listOf("Comedy", "Drama", "Romance"),
            listOf("Crime", "Drama"),
            listOf("Crime", "Drama"),
            listOf("Crime", "Drama"),
            listOf("Action"),
        )
    }

    "findByMovie" {
        // when
        val result = moreLikeThisSearchRepository.findByMovie()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Godfather",
            "The Godfather: Part II",
            "The Godfather: Part III",
            "The Testimony",
            "The Bandit",

        )
        result.mappedResults.map { it.genres } shouldBe listOf(
            listOf("Comedy", "Drama", "Romance"),
            listOf("Crime", "Drama"),
            listOf("Crime", "Drama"),
            listOf("Crime", "Drama"),
            listOf("Crime", "Drama"),
        )
    }

    "findByMovies" {
        // when
        val result = moreLikeThisSearchRepository.findByMovies()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "Alice in Wonderland",
            "Alice in Wonderland",
            "Alice in Wonderland",
            "Alice in Wonderland",
            "Alex in Wonderland",

        )
        result.mappedResults.map { it.genres } shouldBe listOf(
            listOf("Adventure", "Family", "Fantasy"),
            listOf("Adventure", "Family", "Fantasy"),
            listOf("Adventure", "Comedy", "Family"),
            listOf("Adventure", "Family", "Fantasy"),
            listOf("Comedy", "Drama"),
        )
    }
})
