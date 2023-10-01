package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class UnsetRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val unsetRepository = UnsetRepository(mongoTemplate)

    beforeSpec {
        val books = listOf(
            mapOf(
                "_id" to 1,
                "title" to "Antelope Antics",
                "isbn" to "0001122223334",
                "author" to mapOf("last" to "An", "first" to "Auntie"),
                "copies" to listOf(
                    mapOf("warehouse" to "A", "qty" to 5),
                    mapOf("warehouse" to "B", "qty" to 15),
                ),
            ),
            mapOf(
                "_id" to 2,
                "title" to "Bees Babble",
                "isbn" to "999999999333",
                "author" to mapOf("last" to "Bumble", "first" to "Bee"),
                "copies" to listOf(
                    mapOf("warehouse" to "A", "qty" to 2),
                    mapOf("warehouse" to "B", "qty" to 5),
                ),
            ),
        )
        mongoTemplate.insert(books, UnsetRepository.BOOKS)
    }

    "unsetCopies" {
        // when
        val result = unsetRepository.unsetCopies()

        // then
        result.mappedResults shouldBe listOf(
            Document(
                mapOf(
                    "_id" to 1,
                    "title" to "Antelope Antics",
                    "isbn" to "0001122223334",
                    "author" to mapOf("last" to "An", "first" to "Auntie"),
                ),
            ),
            Document(
                mapOf(
                    "_id" to 2,
                    "title" to "Bees Babble",
                    "isbn" to "999999999333",
                    "author" to mapOf("last" to "Bumble", "first" to "Bee"),
                ),
            ),
        )
    }

    "unsetIsbnAndCopies" {
        // when
        val result = unsetRepository.unsetIsbnAndCopies()

        // then
        result.mappedResults shouldBe listOf(
            Document(
                mapOf(
                    "_id" to 1,
                    "title" to "Antelope Antics",
                    "author" to mapOf("last" to "An", "first" to "Auntie"),
                ),
            ),
            Document(
                mapOf(
                    "_id" to 2,
                    "title" to "Bees Babble",
                    "author" to mapOf("last" to "Bumble", "first" to "Bee"),
                ),
            ),
        )
    }

    "unsetEmbeddedFields" {
        // when
        val result = unsetRepository.unsetEmbeddedFields()

        // then
        result.mappedResults shouldBe listOf(
            Document(
                mapOf(
                    "_id" to 1,
                    "title" to "Antelope Antics",
                    "author" to mapOf("last" to "An"),
                    "copies" to listOf(
                        mapOf("qty" to 5),
                        mapOf("qty" to 15),
                    ),
                ),
            ),
            Document(
                mapOf(
                    "_id" to 2,
                    "title" to "Bees Babble",
                    "author" to mapOf("last" to "Bumble"),
                    "copies" to listOf(
                        mapOf("qty" to 2),
                        mapOf("qty" to 5),
                    ),
                ),
            ),
        )
    }
})
