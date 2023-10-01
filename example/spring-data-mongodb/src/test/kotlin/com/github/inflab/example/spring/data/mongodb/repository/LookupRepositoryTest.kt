package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class LookupRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val lookupRepository = LookupRepository(mongoTemplate)

    beforeSpec {
        val classes = listOf(
            mapOf("_id" to 1, "title" to "Reading is ...", "enrollmentlist" to listOf("giraffe2", "pandabear", "artie"), "days" to listOf("M", "W", "F")),
            mapOf("_id" to 2, "title" to "But Writing ...", "enrollmentlist" to listOf("giraffe1", "artie"), "days" to listOf("T", "F")),
        ).map(::Document)
        mongoTemplate.insert(classes, LookupRepository.CLASSES)

        val members = listOf(
            mapOf("_id" to 1, "name" to "artie", "joined" to "2016-05-01", "status" to "A"),
            mapOf("_id" to 2, "name" to "giraffe", "joined" to "2017-05-01", "status" to "D"),
            mapOf("_id" to 3, "name" to "giraffe1", "joined" to "2017-10-01", "status" to "A"),
            mapOf("_id" to 4, "name" to "panda", "joined" to "2018-10-11", "status" to "A"),
            mapOf("_id" to 5, "name" to "pandabear", "joined" to "2018-12-01", "status" to "A"),
            mapOf("_id" to 6, "name" to "giraffe2", "joined" to "2018-12-01", "status" to "D"),
        ).map(::Document)
        mongoTemplate.insert(members, LookupRepository.MEMBERS)

        val restaurants = listOf(
            mapOf("_id" to 1, "name" to "American Steak House", "food" to listOf("filet", "sirloin"), "beverages" to listOf("beer", "wine")),
            mapOf("_id" to 2, "name" to "Honest John Pizza", "food" to listOf("cheese pizza", "pepperoni pizza"), "beverages" to listOf("soda")),
        ).map(::Document)
        mongoTemplate.insert(restaurants, LookupRepository.RESTAURANTS)

        val orders = listOf(
            mapOf("_id" to 1, "item" to "filet", "restaurant_name" to "American Steak House"),
            mapOf("_id" to 2, "item" to "cheese pizza", "restaurant_name" to "Honest John Pizza", "drink" to "lemonade"),
            mapOf("_id" to 3, "item" to "cheese pizza", "restaurant_name" to "Honest John Pizza", "drink" to "soda"),
        ).map(::Document)
        mongoTemplate.insert(orders, LookupRepository.ORDERS)
    }

    "joinWithArray" {
        // when
        val result = lookupRepository.joinWithArray()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(
            "1",
            "2",
        )
        result.mappedResults.map { dto -> dto.enrolleeInfo.map { it.id } } shouldBe listOf(
            listOf("1", "5", "6"),
            listOf("1", "3"),
        )
    }

    "joinWithSubquery" {
        // when
        val result = lookupRepository.joinWithSubquery()

        // then
        result.mappedResults.map { it.id } shouldBe listOf(
            1,
            2,
            3,
        )
        result.mappedResults.map { dto -> dto.matches.map { it.id } } shouldBe listOf(
            emptyList(),
            emptyList(),
            listOf(2),
        )
    }
})
