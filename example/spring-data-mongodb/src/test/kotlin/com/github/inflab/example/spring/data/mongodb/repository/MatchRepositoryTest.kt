package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.types.ObjectId

internal class MatchRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val matchRepository = MatchRepository(mongoTemplate)

    beforeSpec {
        val articles = listOf(
            MatchRepository.Articles(
                id = ObjectId("512bc95fe835e68f199c8686"),
                author = "dave",
                score = 80,
                views = 100,
            ),
            MatchRepository.Articles(
                id = ObjectId("512bc962e835e68f199c8687"),
                author = "dave",
                score = 85,
                views = 521,
            ),
            MatchRepository.Articles(
                id = ObjectId("55f5a192d4bede9ac365b257"),
                author = "ahn",
                score = 60,
                views = 1000,
            ),
            MatchRepository.Articles(
                id = ObjectId("55f5a192d4bede9ac365b258"),
                author = "li",
                score = 55,
                views = 5000,
            ),
            MatchRepository.Articles(
                id = ObjectId("55f5a1d3d4bede9ac365b259"),
                author = "annT",
                score = 60,
                views = 50,
            ),
            MatchRepository.Articles(
                id = ObjectId("55f5a1d3d4bede9ac365b25a"),
                author = "li",
                score = 94,
                views = 999,
            ),
            MatchRepository.Articles(
                id = ObjectId("55f5a1d3d4bede9ac365b25b"),
                author = "ty",
                score = 95,
                views = 1000,
            ),
        )
        mongoTemplate.insertAll(articles)
    }

    "findByAuthor" {
        // when
        val result = matchRepository.findByAuthor()

        // then
        result.mappedResults.map { it.author } shouldBe listOf("dave", "dave")
    }

    "count" {
        // when
        val result = matchRepository.count()

        // then
        result.mappedResults.map { it.count } shouldBe listOf(5)
    }
})
