package com.github.inflab.example.spring.data.mongodb.search.text

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class TextSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class FindTitleSuferDto(
        val title: String,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#basic-example">Text Basic Example</a>
     */
    fun findTitleSufer(): AggregationResults<FindTitleSuferDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("surfer")
                }
            }
        }

        return mongoTemplate.aggregate<Movies, FindTitleSuferDto>(aggregation)
    }
}
