package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.gt
import org.springframework.data.mongodb.core.query.gte
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class MatchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    @Document("articles")
    data class Articles(val id: ObjectId, val author: String, val score: Int, val views: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/match/#equality-match">Equality Match</a>
     */
    fun findByAuthor(): AggregationResults<Articles> {
        val aggregation = aggregation {
            match(Articles::author isEqualTo "dave")
        }

        return mongoTemplate.aggregate<Articles, Articles>(aggregation)
    }

    data class CountDto(val id: Long?, val count: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/match/#perform-a-count">Perform a Count</a>
     */
    fun count(): AggregationResults<CountDto> {
        val aggregation = aggregation {
            match(
                Criteria().orOperator(
                    (Articles::score gt 70).lt(90),
                    Articles::views gte 1000,
                ),
            )
            group {
                idNull()
                "count" accumulator {
                    sum()
                }
            }
        }

        return mongoTemplate.aggregate<Articles, CountDto>(aggregation)
    }
}
