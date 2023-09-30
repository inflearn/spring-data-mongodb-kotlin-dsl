package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.extension.AtlasTest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldBeMonotonicallyDecreasing
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

@AtlasTest(database = "sample_supplies")
internal class EmbeddedDocumentSearchRepositoryTest(
    private val embeddedDocumentSearchRepository: EmbeddedDocumentSearchRepository,
) : FreeSpec({

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
