package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class AddFieldsRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val addFieldsRepository = AddFieldsRepository(mongoTemplate)

    "addFieldsToEmbeddedDocument" {
        // given
        val documents = listOf(
            mapOf("_id" to 1, "type" to "car", "specs" to mapOf("doors" to 4, "wheels" to 4)),
            mapOf("_id" to 2, "type" to "motorcycle", "specs" to mapOf("doors" to 0, "wheels" to 2)),
            mapOf("_id" to 3, "type" to "jet ski"),
        ).map(::Document)

        mongoTemplate.insert(documents, AddFieldsRepository.VEHICLES)

        // when
        val result = addFieldsRepository.addFieldsToEmbeddedDocument().mappedResults

        // then
        result[0] shouldBe AddFieldsRepository.Vehicle(
            id = 1,
            type = "car",
            specs = AddFieldsRepository.Specs(
                doors = 4,
                wheels = 4,
                fuelType = "unleaded",
            ),
        )
        result[1] shouldBe AddFieldsRepository.Vehicle(
            id = 2,
            type = "motorcycle",
            specs = AddFieldsRepository.Specs(
                doors = 0,
                wheels = 2,
                fuelType = "unleaded",
            ),
        )
        result[2] shouldBe AddFieldsRepository.Vehicle(
            id = 3,
            type = "jet ski",
            specs = AddFieldsRepository.Specs(
                doors = null,
                wheels = null,
                fuelType = "unleaded",
            ),
        )
    }

    "overwriteAnExistingFieldWithValue" {
        // given
        val document = mapOf("_id" to 1, "dogs" to 10, "cats" to 15).let(::Document)
        mongoTemplate.insert(document, AddFieldsRepository.ANIMALS)

        // when
        val result = addFieldsRepository.overwriteAnExistingFieldWithValue().mappedResults

        // then
        result[0] shouldBe AddFieldsRepository.Animal(
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
        mongoTemplate.insert(documents, AddFieldsRepository.FRUITS)

        // when
        val result = addFieldsRepository.overwriteAnExistingFieldWithField().mappedResults

        // then
        result[0] shouldBe AddFieldsRepository.Fruit(
            id = "tangerine",
            item = "fruit",
            type = "citrus",
        )
        result[1] shouldBe AddFieldsRepository.Fruit(
            id = "lemon",
            item = "fruit",
            type = "citrus",
        )
        result[2] shouldBe AddFieldsRepository.Fruit(
            id = "grapefruit",
            item = "fruit",
            type = "citrus",
        )
    }
})
