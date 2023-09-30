package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.query.TextCriteria
import org.springframework.stereotype.Repository

@Repository
class SortRepository(
    private val mongoTemplate: MongoTemplate,
) {

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sort/#sort-consistency">Sort Consistency</a>
     */
    fun sortByBorough(): AggregationResults<Document> {
        val aggregation = aggregation {
            sort {
                "borough" by Ascending
                "_id" by Ascending
            }
        }

        return mongoTemplate.aggregate(aggregation, RESTAURANTS, Document::class.java)
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
                "score" by TextScore
                "posts" by Descending
            }
        }

        return mongoTemplate.aggregate(aggregation, USERS, Document::class.java)
    }

    companion object {
        const val RESTAURANTS = "restaurants"
        const val USERS = "users"
    }
}
