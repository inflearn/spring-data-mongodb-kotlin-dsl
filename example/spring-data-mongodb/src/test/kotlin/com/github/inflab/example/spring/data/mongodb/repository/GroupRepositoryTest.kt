package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class GroupRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val groupRepository = GroupRepository(mongoTemplate)

    beforeSpec {
        val sales = listOf(
            GroupRepository.Sales(
                id = 1,
                item = "abc",
                price = 10.0,
                quantity = 2,
                date = LocalDateTime.of(2014, 3, 1, 8, 0, 0),
            ),
            GroupRepository.Sales(
                id = 2,
                item = "jkl",
                price = 20.0,
                quantity = 1,
                date = LocalDateTime.of(2014, 3, 1, 9, 0, 0),
            ),
            GroupRepository.Sales(
                id = 3,
                item = "xyz",
                price = 5.0,
                quantity = 10,
                date = LocalDateTime.of(2014, 3, 15, 9, 0, 0),
            ),
            GroupRepository.Sales(
                id = 4,
                item = "xyz",
                price = 5.0,
                quantity = 20,
                date = LocalDateTime.of(2014, 4, 4, 11, 21, 39, 736000000),
            ),
            GroupRepository.Sales(
                id = 5,
                item = "abc",
                price = 10.0,
                quantity = 10,
                date = LocalDateTime.of(2014, 4, 4, 21, 23, 13, 331000000),
            ),
            GroupRepository.Sales(
                id = 6,
                item = "def",
                price = 7.5,
                quantity = 5,
                date = LocalDateTime.of(2015, 6, 4, 5, 8, 13),
            ),
            GroupRepository.Sales(
                id = 7,
                item = "def",
                price = 7.5,
                quantity = 10,
                date = LocalDateTime.of(2015, 9, 10, 8, 43, 0),
            ),
            GroupRepository.Sales(
                id = 8,
                item = "abc",
                price = 10.0,
                quantity = 5,
                date = LocalDateTime.of(2016, 2, 6, 20, 20, 13),
            ),
        )
        mongoTemplate.insertAll(sales)
    }

    "count" {
        // when
        val result = groupRepository.count()

        // then
        result.mappedResults.map { it.count } shouldBe listOf(8)
    }

    "groupByItemHaving" {
        // when
        val result = groupRepository.groupByItemHaving()

        // then
        result.mappedResults.toSet() shouldBe setOf(
            GroupRepository.GroupByItemHavingDto(id = "abc", totalSaleAmount = 170.0),
            GroupRepository.GroupByItemHavingDto(id = "xyz", totalSaleAmount = 150.0),
            GroupRepository.GroupByItemHavingDto(id = "def", totalSaleAmount = 112.5),
        )
    }

    "groupByDayAndYear" {
        // when
        val result = groupRepository.groupByDayAndYear()

        // then
        result.mappedResults.take(2) shouldBe listOf(
            GroupRepository.GroupByDayAndYearDto(
                id = "2014-04-04",
                totalSaleAmount = 200.0,
                averageQuantity = 15.0,
                count = 2,
            ),
            GroupRepository.GroupByDayAndYearDto(
                id = "2014-03-15",
                totalSaleAmount = 50.0,
                averageQuantity = 10.0,
                count = 1,
            ),
        )
    }
})
