package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class RegexSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {
    data class TitleDto(val title: String)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/regex/#examples">Example</a>
     */
    fun findEndSeattleTitle(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                regex {
                    path("title")
                    query("(.*) Seattle")
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
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/regex/#examples">Example</a>
     */
    fun findRegexTitle(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                regex {
                    path("title")
                    query("[0-9]{2} (.){4}s")
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
