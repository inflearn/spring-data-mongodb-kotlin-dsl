package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.Document
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Repository

@Repository
class SortRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Restaurant(
        @Id val id: Long,
        val name: String,
        val borough: String,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sort/#sort-consistency">Sort Consistency</a>
     */
    fun sortByBorough(): AggregationResults<Restaurant> {
        val aggregation = aggregation {
            sort {
                Restaurant::borough by asc
                Restaurant::id by asc
            }
        }

        return mongoTemplate.aggregate<Restaurant>(aggregation, RESTAURANTS)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sort/#text-score-metadata-sort">Text Score Metadata Sort</a>
     */
    fun sortByScore(): AggregationResults<Document> {
        val aggregation = aggregation {
            match(
                TextCriteria.forDefaultLanguage().matching("operating"),
            )

            sort {
                "score" by textScore
                "posts" by desc
            }
        }

        return mongoTemplate.aggregate(aggregation, USERS, Document::class.java)
    }

    companion object {
        const val RESTAURANTS = "restaurants"
        const val USERS = "users"
    }
}
