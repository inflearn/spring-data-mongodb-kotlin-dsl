package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class UnionWithRepositoryTest : FreeSpec({
    val mongoTemplate = makeMongoTemplate()
    val unionWithRepository = UnionWithRepository(mongoTemplate)

    beforeSpec {
        val sales2017 = listOf(
            mapOf("store" to "General Store", "item" to "Chocolates", "quantity" to 150),
            mapOf("store" to "ShopMart", "item" to "Chocolates", "quantity" to 50),
            mapOf("store" to "General Store", "item" to "Cookies", "quantity" to 100),
            mapOf("store" to "ShopMart", "item" to "Cookies", "quantity" to 120),
            mapOf("store" to "General Store", "item" to "Pie", "quantity" to 10),
            mapOf("store" to "ShopMart", "item" to "Pie", "quantity" to 5),
        ).map(::Document)
        mongoTemplate.insert(sales2017, UnionWithRepository.SALES_2017)

        val sales2018 = listOf(
            mapOf("store" to "General Store", "item" to "Cheese", "quantity" to 30),
            mapOf("store" to "ShopMart", "item" to "Cheese", "quantity" to 50),
            mapOf("store" to "General Store", "item" to "Chocolates", "quantity" to 125),
            mapOf("store" to "ShopMart", "item" to "Chocolates", "quantity" to 150),
            mapOf("store" to "General Store", "item" to "Cookies", "quantity" to 200),
            mapOf("store" to "ShopMart", "item" to "Cookies", "quantity" to 100),
            mapOf("store" to "ShopMart", "item" to "Nuts", "quantity" to 100),
            mapOf("store" to "General Store", "item" to "Pie", "quantity" to 30),
            mapOf("store" to "ShopMart", "item" to "Pie", "quantity" to 25),
        ).map(::Document)
        mongoTemplate.insert(sales2018, UnionWithRepository.SALES_2018)

        val sales2019 = listOf(
            mapOf("store" to "General Store", "item" to "Cheese", "quantity" to 50),
            mapOf("store" to "ShopMart", "item" to "Cheese", "quantity" to 20),
            mapOf("store" to "General Store", "item" to "Chocolates", "quantity" to 125),
            mapOf("store" to "ShopMart", "item" to "Chocolates", "quantity" to 150),
            mapOf("store" to "General Store", "item" to "Cookies", "quantity" to 200),
            mapOf("store" to "ShopMart", "item" to "Cookies", "quantity" to 100),
            mapOf("store" to "General Store", "item" to "Nuts", "quantity" to 80),
            mapOf("store" to "ShopMart", "item" to "Nuts", "quantity" to 30),
            mapOf("store" to "General Store", "item" to "Pie", "quantity" to 50),
            mapOf("store" to "ShopMart", "item" to "Pie", "quantity" to 75),
        ).map(::Document)
        mongoTemplate.insert(sales2019, UnionWithRepository.SALES_2019)

        val sales2020 = listOf(
            mapOf("store" to "General Store", "item" to "Cheese", "quantity" to 100),
            mapOf("store" to "ShopMart", "item" to "Cheese", "quantity" to 100),
            mapOf("store" to "General Store", "item" to "Chocolates", "quantity" to 200),
            mapOf("store" to "ShopMart", "item" to "Chocolates", "quantity" to 300),
            mapOf("store" to "General Store", "item" to "Cookies", "quantity" to 500),
            mapOf("store" to "ShopMart", "item" to "Cookies", "quantity" to 400),
            mapOf("store" to "General Store", "item" to "Nuts", "quantity" to 100),
            mapOf("store" to "ShopMart", "item" to "Nuts", "quantity" to 200),
            mapOf("store" to "General Store", "item" to "Pie", "quantity" to 100),
            mapOf("store" to "ShopMart", "item" to "Pie", "quantity" to 100),
        ).map(::Document)
        mongoTemplate.insert(sales2020, UnionWithRepository.SALES_2020)
    }

    "findSalesByYearAndStoresAndItems" {
        // when
        val result = unionWithRepository.findSalesByYearAndStoresAndItems()

        // then
        result.mappedResults.map { it.id to it.quantity } shouldBe listOf(
            "2017" to 150,
            "2017" to 100,
            "2017" to 10,
            "2017" to 50,
            "2017" to 120,
            "2017" to 5,
            "2018" to 30,
            "2018" to 125,
            "2018" to 200,
            "2018" to 30,
            "2018" to 50,
            "2018" to 150,
            "2018" to 100,
            "2018" to 100,
            "2018" to 25,
            "2019" to 50,
            "2019" to 125,
            "2019" to 200,
            "2019" to 80,
            "2019" to 50,
            "2019" to 20,
            "2019" to 150,
            "2019" to 100,
            "2019" to 30,
            "2019" to 75,
            "2020" to 100,
            "2020" to 200,
            "2020" to 500,
            "2020" to 100,
            "2020" to 100,
            "2020" to 100,
            "2020" to 300,
            "2020" to 400,
            "2020" to 200,
            "2020" to 100,
        )
    }

    "findSalesBYItems" {
        // when
        val result = unionWithRepository.findSalesByItems()

        // then
        result.mappedResults.map { it.id to it.total } shouldBe listOf(
            "Cookies" to 1720,
            "Chocolates" to 1250,
            "Nuts" to 510,
            "Pie" to 395,
            "Cheese" to 350,
        )
    }
})
