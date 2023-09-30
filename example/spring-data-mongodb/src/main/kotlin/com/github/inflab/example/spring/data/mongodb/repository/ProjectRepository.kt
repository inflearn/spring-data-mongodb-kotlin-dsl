package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.REMOVE
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators
import org.springframework.stereotype.Repository

@Repository
class ProjectRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Author(val first: String, val last: String, val middle: String?)
    data class TitleAndAuthorDto(
        val id: String,
        val title: String,
        val author: Author,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/project/#conditionally-exclude-fields">Conditionally Exclude Fields</a>
     */
    fun excludeConditionally(): AggregationResults<TitleAndAuthorDto> {
        val aggregation = aggregation {
            project {
                +"title"
                "author.first" alias "author.first"
                "author.last" alias "author.last"
                "author.middle" expression ConditionalOperators.`when`(
                    ComparisonOperators.valueOf("author.middle").equalToValue(""),
                ).thenValueOf(REMOVE).otherwiseValueOf("author.middle")
            }
        }

        return mongoTemplate.aggregate(aggregation, BOOKS, TitleAndAuthorDto::class.java)
    }

    companion object {
        const val BOOKS = "books"
    }
}
