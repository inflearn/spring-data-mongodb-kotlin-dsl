package com.github.inflab.example.spring.data.mongodb.search.phrase

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class PhraseSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndScoreDto(
        val title: String,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/phrase/#single-phrase-example">Single Phrase Example</a>
     */
    fun findTitleWithNewYork(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                phrase {
                    path(Movies::title)
                    query("new york")
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/phrase/#multiple-phrases-example">Multiple Phrases Example</a>
     */
    fun findTitleWithManAndMoon(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                phrase {
                    path(Movies::title)
                    query("the man", "the moon")
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/phrase/#slop-example">Slop Example</a>
     */
    fun findTitleWithMenWomenBySlop(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                phrase {
                    path(Movies::title)
                    query("men women")
                    slop = 5
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
}
