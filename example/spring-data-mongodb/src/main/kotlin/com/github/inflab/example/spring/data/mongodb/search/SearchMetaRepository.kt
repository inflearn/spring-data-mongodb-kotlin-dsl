package com.github.inflab.example.spring.data.mongodb.search

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults

class SearchMetaRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Count(val total: Long)
    data class CountDto(val count: Count)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#example">Example</a>
     */
    fun findCountBetweenYear(): AggregationResults<CountDto> {
        val aggregation = aggregation {
            searchMeta {
                range {
                    path("year")
                    gte(1998)
                    lt(1999)
                }
                totalCount()
            }
        }

        return mongoTemplate.aggregate<Movies, CountDto>(aggregation)
    }
}
