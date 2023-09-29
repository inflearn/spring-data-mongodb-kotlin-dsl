package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class WildcardSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {
    data class TitleDto(val title: String)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/wildcard/#examples">Example</a>
     */
    fun findTitleWithGreenD(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                wildcard {
                    path(Movies::title)
                    query("Green D*")
                }
            }
            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/wildcard/#escape-character-example">Escape Character Example</a>
     */
    fun findTitleWithQuestionMark(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                wildcard {
                    path(Movies::title)
                    query("*\\?")
                }
            }
            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }
}
