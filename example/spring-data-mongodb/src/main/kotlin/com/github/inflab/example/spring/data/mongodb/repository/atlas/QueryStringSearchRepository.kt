package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class QueryStringSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class PlotAndFullplotDto(
        val title: String,
        val plot: String,
        val fullplot: String,
        val score: Double,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#boolean-operator-queries">Boolean Operator Queries (OR AND)</a>
     */
    fun findFullplotWithPlot(): AggregationResults<PlotAndFullplotDto> {
        val aggregation = aggregation {
            search {
                queryString {
                    defaultPath(Movies::fullplot)
                    query {
                        sub(text("captain") or text("kirk"), PlotAndFullplotDto::plot) and text("enterprise")
                    }
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(3))

            project {
                excludeId()
                +Movies::title
                +Movies::plot
                +Movies::fullplot
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, PlotAndFullplotDto>(aggregation)
    }

    data class TitleDto(val title: String)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#range-queries">Range Queries (TO)</a>
     */
    fun findPoltWithTitleRange(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                queryString {
                    defaultPath(Movies::plot)
                    query {
                        range(left = "count", right = wildcard, leftInclusion = true, rightInclusion = true, PlotAndFullplotDto::title)
                    }
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(10))

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#wildcard-queries">Wildcard Queries (Fuzzy)</a>
     */
    fun findTitleByFuzzy(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                queryString {
                    defaultPath(Movies::title)
                    query {
                        fuzzy("catch", 2)
                    }
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(10))

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#wildcard-queries">Wildcard Queries (Wildcard)</a>
     */
    fun findTitleByWildcard(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                queryString {
                    defaultPath(Movies::title)
                    query {
                        wildcard("cou*t?*")
                    }
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#wildcard-queries">Wildcard Queries (Regex)</a>
     */
    fun findTitleByRegex(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                queryString {
                    defaultPath(Movies::title)
                    query {
                        regex(".tal(y|ian)")
                    }
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }
}
