package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document
import org.springframework.data.mongodb.core.index.TextIndexDefinition

internal class SortRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val sortRepository = SortRepository(mongoTemplate)

    beforeSpec {
        val documents = listOf(
            mapOf("_id" to 1, "name" to "Central Park Cafe", "borough" to "Manhattan"),
            mapOf("_id" to 2, "name" to "Rock A Feller Bar and Grill", "borough" to "Queens"),
            mapOf("_id" to 3, "name" to "Empire State Pub", "borough" to "Brooklyn"),
            mapOf("_id" to 4, "name" to "Stan's Pizzaria", "borough" to "Manhattan"),
            mapOf("_id" to 5, "name" to "Jane's Deli", "borough" to "Brooklyn"),
        ).map(::Document)

        mongoTemplate.insert(documents, SortRepository.RESTAURANTS)

        val users = listOf(
            mapOf("_id" to 1, "name" to "john", "posts" to 10),
            mapOf("_id" to 2, "name" to "jane", "posts" to 5),
            mapOf("_id" to 3, "name" to "OPERATING", "posts" to 10),
            mapOf("_id" to 4, "name" to "operating", "posts" to 50),
            mapOf("_id" to 5, "name" to "paul operating test", "posts" to 30),
        ).map(::Document)

        mongoTemplate.insert(users, SortRepository.USERS)
        mongoTemplate.indexOps(SortRepository.USERS).ensureIndex(TextIndexDefinition.builder().onField("name").build())
    }

    "sortByBorough" {
        // when
        val result = sortRepository.sortByBorough()

        // then
        result.mappedResults.map { it["_id"] } shouldBe listOf(3, 5, 1, 4, 2)
    }

    "sortByScore" {
        // when
        val result = sortRepository.sortByScore()

        // then
        result.mappedResults.map { it["_id"] } shouldBe listOf(4, 3, 5)
    }
})
