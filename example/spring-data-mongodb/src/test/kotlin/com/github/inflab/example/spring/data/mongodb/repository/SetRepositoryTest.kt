package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

class SetRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val setRepository = SetRepository(mongoTemplate)

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
})
