package com.github.inflab.example.spring.data.mongodb.search

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

    data class TitleAndScoreDto(
        val title: String,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#basic-example">Basic Example</a>
     */
    fun findTitleWithSufer(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("surfer")
                }
            }

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#fuzzy-examples">Fuzzy Example</a>
     */
    fun findTitleWithNawYarkByFuzzy(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("naw yark")
                    fuzzy(maxEdits = 1, prefixLength = 2)
                }
            }

            // TODO: add $limit stage

            project {
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }
}
