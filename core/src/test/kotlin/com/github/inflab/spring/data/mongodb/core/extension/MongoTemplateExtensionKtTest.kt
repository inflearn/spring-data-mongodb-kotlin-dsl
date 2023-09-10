package com.github.inflab.spring.data.mongodb.core.extension

import com.github.inflab.spring.data.mongodb.core.aggregation.AggregationDsl
import io.kotest.core.spec.style.FreeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation

internal class MongoTemplateExtensionKtTest : FreeSpec({
    data class TestCollection(val id: String)
    data class TestResult(val count: Int)

    "should call aggregate with aggregation and collectionName" {
        // given
        val mongoTemplate = mockk<MongoTemplate>()
        val aggregation: AggregationDsl.() -> Unit = { count("count") }
        val collectionName = "SimpleCollection"

        every { mongoTemplate.aggregate(any<Aggregation>(), any<String>(), any<Class<*>>()) } returns mockk()

        // when
        mongoTemplate.aggregate<TestCollection, TestResult>(
            aggregation = aggregation,
            collectionName = collectionName,
        )

        // then
        verify {
            mongoTemplate.aggregate(
                any<Aggregation>(),
                collectionName,
                TestResult::class.java,
            )
        }
    }

    "should call aggregate with aggregation and collectionName from class" {
        // given
        val mongoTemplate = mockk<MongoTemplate>()
        val aggregation: AggregationDsl.() -> Unit = { count("count") }
        val collectionName = TestCollection::class.java.simpleName

        every { mongoTemplate.getCollectionName(any()) } returns collectionName
        every { mongoTemplate.aggregate(any<Aggregation>(), any<String>(), any<Class<*>>()) } returns mockk()

        // when
        mongoTemplate.aggregate<TestCollection, TestResult>(
            aggregation = aggregation,
        )

        // then
        verifySequence {
            mongoTemplate.getCollectionName(TestCollection::class.java)
            mongoTemplate.aggregate(
                any<Aggregation>(),
                collectionName,
                TestResult::class.java,
            )
        }
    }
})
