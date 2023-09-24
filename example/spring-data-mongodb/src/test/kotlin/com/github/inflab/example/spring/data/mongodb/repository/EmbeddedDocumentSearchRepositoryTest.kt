package com.github.inflab.example.spring.data.mongodb.repository

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory

@Ignored
internal class EmbeddedDocumentSearchRepositoryTest : FreeSpec({
    val connectionString = "mongodb+srv://<username>:<password>@<host>/sample_supplies?retryWrites=true&w=majority"
    val mongoTemplate = MongoTemplate(SimpleMongoClientDatabaseFactory(connectionString))
    val embeddedDocumentSearchRepository = EmbeddedDocumentSearchRepository(mongoTemplate)

    "findItemsByMean" {
        // when
        val result = embeddedDocumentSearchRepository.findItemsByMean()

        // then
        result.mappedResults.take(5).flatMap { it.items } shouldBe listOf(
            EmbeddedDocumentSearchRepository.Item("backpack", listOf("school", "travel", "kids")),
            EmbeddedDocumentSearchRepository.Item("envelopes", listOf("stationary", "office", "general")),
            EmbeddedDocumentSearchRepository.Item("printer paper", listOf("office", "stationary")),
            EmbeddedDocumentSearchRepository.Item("backpack", listOf("school", "travel", "kids")),
            EmbeddedDocumentSearchRepository.Item("backpack", listOf("school", "travel", "kids")),
            EmbeddedDocumentSearchRepository.Item("backpack", listOf("school", "travel", "kids")),
            EmbeddedDocumentSearchRepository.Item("backpack", listOf("school", "travel", "kids")),
        )
        result.mappedResults.take(5).map { it.score }.shouldBeMonotonicallyDecreasing()
    }

    "findItemsByFacet" {
        // when
        val result = embeddedDocumentSearchRepository.findItemsByFacet()

        // then
        result.mappedResults.first().count.lowerBound.shouldBeGreaterThan(0)
        result.mappedResults.first().facet.purchaseMethodFacet.buckets shouldBe listOf(
            EmbeddedDocumentSearchRepository.Bucket("In store", 2751),
            EmbeddedDocumentSearchRepository.Bucket("Online", 1535),
            EmbeddedDocumentSearchRepository.Bucket("Phone", 578),
        )
    }
})
