package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class ReplaceRootRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val replaceRootRepository = ReplaceRootRepository(mongoTemplate)

    "replaceRootWithEmbeddedDocumentField" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "name" to "Arlene", "age" to 34, "pets" to mapOf("dogs" to 2, "cats" to 1)),
            mapOf("_id" to 2, "name" to "Sam", "age" to 41, "pets" to mapOf("cats" to 1, "fish" to 3)),
            mapOf("_id" to 3, "name" to "Maria", "age" to 25),
        ).map(::Document)

        mongoTemplate.insert(documents, ReplaceRootRepository.PEOPLE)

        // when
        val result = replaceRootRepository.replaceRootWithEmbeddedDocumentField()

        // then
        result.mappedResults shouldHaveSize 3
        result.mappedResults[0] shouldBe ReplaceRootRepository.Pet(
            dogs = 2,
            cats = 1,
            birds = 0,
            fish = 0,
        )
        result.mappedResults[1] shouldBe ReplaceRootRepository.Pet(
            dogs = 0,
            cats = 1,
            birds = 0,
            fish = 3,
        )
        result.mappedResults[2] shouldBe ReplaceRootRepository.Pet(
            dogs = 0,
            cats = 0,
            birds = 0,
            fish = 0,
        )
    }

    "replaceRootWithNestedArray" {
        // given
        val document = listOf(
            mapOf(
                "_id" to 1,
                "grades" to listOf(
                    mapOf("test" to 1, "grade" to 80, "mean" to 75, "std" to 6),
                    mapOf("test" to 2, "grade" to 85, "mean" to 90, "std" to 4),
                    mapOf("test" to 3, "grade" to 95, "mean" to 85, "std" to 6),
                ),
            ),
            mapOf(
                "_id" to 2,
                "grades" to listOf(
                    mapOf("test" to 1, "grade" to 90, "mean" to 75, "std" to 6),
                    mapOf("test" to 2, "grade" to 87, "mean" to 90, "std" to 3),
                    mapOf("test" to 3, "grade" to 91, "mean" to 85, "std" to 4),
                ),
            ),
        ).map(::Document)

        mongoTemplate.insert(document, ReplaceRootRepository.STUDENT)

        // when
        val result = replaceRootRepository.replaceRootWithNestedArray()

        // then
        result.mappedResults shouldHaveSize 3
        result.mappedResults[0] shouldBe ReplaceRootRepository.Grade(
            test = 3,
            grade = 95,
            mean = 85,
            std = 6,
        )
        result.mappedResults[1] shouldBe ReplaceRootRepository.Grade(
            test = 1,
            grade = 90,
            mean = 75,
            std = 6,
        )
        result.mappedResults[2] shouldBe ReplaceRootRepository.Grade(
            test = 3,
            grade = 91,
            mean = 85,
            std = 4,
        )
    }

    "replaceRootWithNewlyCreatedDocument" {
        // given
        val document = listOf(
            mapOf("_id" to 1, "first_name" to "Gary", "last_name" to "Sheffield", "city" to "New York"),
            mapOf("_id" to 2, "first_name" to "Nancy", "last_name" to "Walker", "city" to "Anaheim"),
            mapOf("_id" to 3, "first_name" to "Peter", "last_name" to "Sumner", "city" to "Toledo"),
        ).map(::Document)

        mongoTemplate.insert(document, ReplaceRootRepository.CONTACT)

        // when
        val result = replaceRootRepository.replaceRootWithNewlyCreatedDocument()

        // then
        result.mappedResults shouldHaveSize 3
        result.mappedResults[0] shouldBe ReplaceRootRepository.FindFullNameResult(
            fullName = "Gary Sheffield",
        )
        result.mappedResults[1] shouldBe ReplaceRootRepository.FindFullNameResult(
            fullName = "Nancy Walker",
        )
        result.mappedResults[2] shouldBe ReplaceRootRepository.FindFullNameResult(
            fullName = "Peter Sumner",
        )
    }

    "replaceRootWithNewlyCreatedDocumentAndDefaultDocument" {
        // given
        val document = listOf(
            mapOf("_id" to 1, "name" to "Fred", "email" to "fred@example.net"),
            mapOf("_id" to 2, "name" to "Frank N. Stine", "cell" to "012-345-9999"),
            mapOf("_id" to 3, "name" to "Gren Dell", "home" to "987-654-3210", "email" to "beo@example.net"),
        ).map(::Document)

        mongoTemplate.insert(document, ReplaceRootRepository.CONTACT)

        // when
        val result = replaceRootRepository.replaceRootWithNewlyCreatedDocumentAndDefaultDocument()

        // then
        result.mappedResults shouldHaveSize 3
        result.mappedResults[0] shouldBe ReplaceRootRepository.ContactInfo(
            id = 1,
            name = "Fred",
            email = "fred@example.net",
            cell = "",
            home = "",
        )
        result.mappedResults[1] shouldBe ReplaceRootRepository.ContactInfo(
            id = 2,
            name = "Frank N. Stine",
            email = "",
            cell = "012-345-9999",
            home = "",
        )
        result.mappedResults[2] shouldBe ReplaceRootRepository.ContactInfo(
            id = 3,
            name = "Gren Dell",
            email = "beo@example.net",
            cell = "",
            home = "987-654-3210",
        )
    }
})
