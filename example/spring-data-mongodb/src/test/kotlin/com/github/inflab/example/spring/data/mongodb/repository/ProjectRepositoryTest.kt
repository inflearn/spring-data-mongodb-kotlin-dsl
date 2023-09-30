package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class ProjectRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val sortByCountRepository = ProjectRepository(mongoTemplate)

    beforeSpec {
        val documents = listOf(
            mapOf("_id" to 1, "title" to "abc123", "isbn" to "0001122223334", "author" to mapOf("last" to "zzz", "first" to "aaa"), "copies" to 5, "lastModified" to "2016-07-28"),
            mapOf("_id" to 2, "title" to "Baked Goods", "isbn" to "9999999999999", "author" to mapOf("last" to "xyz", "first" to "abc", "middle" to ""), "copies" to 2, "lastModified" to "2017-07-21"),
            mapOf("_id" to 3, "title" to "Ice Cream Cakes", "isbn" to "8888888888888", "author" to mapOf("last" to "xyz", "first" to "abc", "middle" to "mmm"), "copies" to 5, "lastModified" to "2017-07-22"),
        ).map(::Document)

        mongoTemplate.insert(documents, ProjectRepository.BOOKS)
    }

    "excludeConditionally" {
        // when
        val result = sortByCountRepository.excludeConditionally()

        // then
        result.mappedResults.map { it.title } shouldBe listOf(
            "abc123",
            "Baked Goods",
            "Ice Cream Cakes",
        )
        result.mappedResults.map { it.author.middle } shouldBe listOf(
            null,
            null,
            "mmm",
        )
    }
})
