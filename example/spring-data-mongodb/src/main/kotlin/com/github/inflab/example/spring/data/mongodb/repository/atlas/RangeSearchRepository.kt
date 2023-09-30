package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class RangeSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndRuntimeDto(
        val title: String,
        val runtime: Int,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/range/#number-example">Number Example</a>
     */
    fun findRuntimeBetween(): AggregationResults<TitleAndRuntimeDto> {
        val aggregation = aggregation {
            search {
                range {
                    path(Movies::runtime)
                    gte(2)
                    lte(3)
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::runtime
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndRuntimeDto>(aggregation)
    }

    data class TitleAndReleasedDto(
        val title: String,
        val released: LocalDateTime,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/range/#date-example">Date Example</a>
     */
    fun findReleasedBetween(): AggregationResults<TitleAndReleasedDto> {
        val aggregation = aggregation {
            search {
                range {
                    path(Movies::released)
                    gt(LocalDate.of(2010, 1, 1))
                    lt(LocalDate.of(2015, 1, 1))
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::released
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndReleasedDto>(aggregation)
    }
}
