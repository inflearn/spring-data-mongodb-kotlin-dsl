package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.bson.Document

class SetRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val setRepository = SetRepository(mongoTemplate)

    "twoSetStages" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "student" to "Maya", "homework" to listOf(10, 5, 10), "quiz" to listOf(10, 8), "extraCredit" to 0),
            mapOf("_id" to 2, "student" to "Ryan", "homework" to listOf(5, 6, 5), "quiz" to listOf(8, 8), "extraCredit" to 8),
        ).map(::Document)

        mongoTemplate.insert(documents, SetRepository.SCORES)

        // when
        val result = setRepository.twoSetStages().mappedResults

        // then
        result[0] shouldBe SetRepository.ScoreWithTotal(
            id = 1,
            student = "Maya",
            homework = listOf(10, 5, 10),
            quiz = listOf(10, 8),
            extraCredit = 0,
            totalHomework = 25,
            totalQuiz = 18,
            totalScore = 43,
        )
        result[1] shouldBe SetRepository.ScoreWithTotal(
            id = 2,
            student = "Ryan",
            homework = listOf(5, 6, 5),
            quiz = listOf(8, 8),
            extraCredit = 8,
            totalHomework = 16,
            totalQuiz = 16,
            totalScore = 40,
        )
    }

    "setToEmbeddedDocument" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "type" to "car", "specs" to mapOf("doors" to 4, "wheels" to 4)),
            mapOf("_id" to 2, "type" to "motorcycle", "specs" to mapOf("doors" to 0, "wheels" to 2)),
            mapOf("_id" to 3, "type" to "jet ski"),
        ).map(::Document)

        mongoTemplate.insert(documents, SetRepository.VEHICLES)

        // when
        val result = setRepository.setToEmbeddedDocument().mappedResults

        // then
        result[0] shouldBe SetRepository.Vehicle(
            id = 1,
            type = "car",
            specs = SetRepository.Specs(
                doors = 4,
                wheels = 4,
                fuelType = "unleaded",
            ),
        )
        result[1] shouldBe SetRepository.Vehicle(
            id = 2,
            type = "motorcycle",
            specs = SetRepository.Specs(
                doors = 0,
                wheels = 2,
                fuelType = "unleaded",
            ),
        )
        result[2] shouldBe SetRepository.Vehicle(
            id = 3,
            type = "jet ski",
            specs = SetRepository.Specs(
                doors = null,
                wheels = null,
                fuelType = "unleaded",
            ),
        )
    }

    "overwriteAnExistingFieldWithValue" {
        // given
        val document = mapOf("_id" to 1, "dogs" to 10, "cats" to 15).let(::Document)
        mongoTemplate.insert(document, SetRepository.ANIMALS)

        // when
        val result = setRepository.overwriteAnExistingFieldWithValue().mappedResults

        // then
        result[0] shouldBe SetRepository.Animal(
            id = 1,
            dogs = 10,
            cats = 20,
        )
    }

    "overwriteAnExistingFieldWithField" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "item" to "tangerine", "type" to "citrus"),
            mapOf("_id" to 2, "item" to "lemon", "type" to "citrus"),
            mapOf("_id" to 3, "item" to "grapefruit", "type" to "citrus"),
        ).map(::Document)
        mongoTemplate.insert(documents, SetRepository.FRUITS)

        // when
        val result = setRepository.overwriteAnExistingFieldWithField().mappedResults

        // then
        result[0] shouldBe SetRepository.Fruit(
            id = "tangerine",
            item = "fruit",
            type = "citrus",
        )
        result[1] shouldBe SetRepository.Fruit(
            id = "lemon",
            item = "fruit",
            type = "citrus",
        )
        result[2] shouldBe SetRepository.Fruit(
            id = "grapefruit",
            item = "fruit",
            type = "citrus",
        )
    }

    "createNewFieldWithExistingFields" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "student" to "Maya", "homework" to listOf(10, 5, 10), "quiz" to listOf(10, 8), "extraCredit" to 0),
            mapOf("_id" to 2, "student" to "Ryan", "homework" to listOf(5, 6, 5), "quiz" to listOf(8, 8), "extraCredit" to 8),
        ).map(::Document)
        mongoTemplate.insert(documents, SetRepository.SCORES)

        // when
        val result = setRepository.createNewFieldWithExistingFields().mappedResults

        // then
        result shouldHaveSize 2
        result[0] shouldBe SetRepository.ScoreWithAvgQuiz(
            id = 1,
            student = "Maya",
            homework = listOf(10, 5, 10),
            quiz = listOf(10, 8),
            extraCredit = 0,
            quizAverage = 9,
        )
        result[1] shouldBe SetRepository.ScoreWithAvgQuiz(
            id = 2,
            student = "Ryan",
            homework = listOf(5, 6, 5),
            quiz = listOf(8, 8),
            extraCredit = 8,
            quizAverage = 8,
        )
    }
})
