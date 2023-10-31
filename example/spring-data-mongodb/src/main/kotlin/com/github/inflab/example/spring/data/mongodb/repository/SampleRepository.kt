package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class SampleRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("user")
    data class User(@Id val id: String, val name: String, val q1: Boolean, val q2: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sample/#example">Example</a>
     */
    fun example(): AggregationResults<User> {
        val aggregation = aggregation {
            sample(3)
        }

        return mongoTemplate.aggregate<User, User>(aggregation)
    }
}
