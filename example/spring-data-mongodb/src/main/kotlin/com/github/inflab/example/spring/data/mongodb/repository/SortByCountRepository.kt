package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class SortByCountRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class IdAndCountDto(val id: String, val count: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sortByCount/#example">Example</a>
     */
    fun sortByTags(): AggregationResults<IdAndCountDto> {
        val aggregation = aggregation {
            unwind { path("tags") }
            sortByCount("tags")
        }

        return mongoTemplate.aggregate(aggregation, EXHIBITS, IdAndCountDto::class.java)
    }

    companion object {
        const val EXHIBITS = "exhibits"
    }
}
