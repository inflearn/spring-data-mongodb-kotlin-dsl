package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class SortByCountRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val sortByCountRepository = SortByCountRepository(mongoTemplate)

    beforeSpec {
        val documents = listOf(
            mapOf("_id" to 1, "title" to "The Pillars of Society", "artist" to "Grosz", "year" to 1926, "tags" to listOf("painting", "satire", "Expressionism", "caricature")),
            mapOf("_id" to 2, "title" to "Melancholy III", "artist" to "Munch", "year" to 1902, "tags" to listOf("woodcut", "Expressionism")),
            mapOf("_id" to 3, "title" to "Dancer", "artist" to "Miro", "year" to 1925, "tags" to listOf("oil", "Surrealism", "painting")),
            mapOf("_id" to 4, "title" to "The Great Wave off Kanagawa", "artist" to "Hokusai", "tags" to listOf("woodblock", "ukiyo-e")),
            mapOf("_id" to 5, "title" to "The Persistence of Memory", "artist" to "Dali", "year" to 1931, "tags" to listOf("Surrealism", "painting", "oil")),
            mapOf("_id" to 6, "title" to "Composition VII", "artist" to "Kandinsky", "year" to 1913, "tags" to listOf("oil", "painting", "abstract")),
            mapOf("_id" to 7, "title" to "The Scream", "artist" to "Munch", "year" to 1893, "tags" to listOf("Expressionism", "painting", "oil")),
            mapOf("_id" to 8, "title" to "Blue Flower", "artist" to "O'Keefe", "year" to 1918, "tags" to listOf("abstract", "painting")),
        ).map(::Document)

        mongoTemplate.insert(documents, SortByCountRepository.EXHIBITS)
    }

    "sortByTags" {
        // when
        val result = sortByCountRepository.sortByTags()

        // then
        result.mappedResults.take(3).map { it.id } shouldBe listOf(
            "painting",
            "oil",
            "Expressionism",
        )
        result.mappedResults.map { it.count } shouldBe listOf(6, 4, 3, 2, 2, 1, 1, 1, 1, 1)
    }
})
