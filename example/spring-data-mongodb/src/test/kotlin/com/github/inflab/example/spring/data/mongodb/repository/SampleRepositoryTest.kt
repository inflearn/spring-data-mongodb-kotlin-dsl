package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.extension.makeMongoTemplate
import com.github.inflab.example.spring.data.mongodb.repository.SampleRepository.User
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.MongoTemplate

internal class SampleRepositoryTest : FreeSpec({
    val mongoTemplate: MongoTemplate = makeMongoTemplate()
    val sampleRepository = SampleRepository(mongoTemplate)

    beforeSpec {
        val documents = listOf<User>(
            User(id = "1", name = "dave123", q1 = true, q2 = true),
            User(id = "2", name = "dave2", q1 = false, q2 = false),
            User(id = "3", name = "ahn", q1 = true, q2 = true),
            User(id = "4", name = "li", q1 = true, q2 = false),
            User(id = "5", name = "annT", q1 = false, q2 = true),
            User(id = "6", name = "li", q1 = true, q2 = true),
            User(id = "7", name = "ty", q1 = false, q2 = true),
        )

        mongoTemplate.insertAll(documents)
    }

    "example" {
        // when
        val result = sampleRepository.example()

        // then
        result.mappedResults.toSet().size shouldBe 3
    }
})
