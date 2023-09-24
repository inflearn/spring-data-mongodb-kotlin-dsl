package com.github.inflab.example.spring.data.mongodb.search

import com.github.inflab.example.spring.data.mongodb.entity.mflix.MovieImdb
import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mapping.div
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class ScoreSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndPlotDto(val title: String, val plot: String, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples">Boost Example</a>
     */
    fun findTitleAndPlotWithBoost(): AggregationResults<TitleAndPlotDto> {
        val aggregation = aggregation {
            search {
                compound {
                    should {
                        text {
                            query("Helsinki")
                            path(Movies::plot)
                        }
                        text {
                            query("Helsinki")
                            path(Movies::title)
                            score {
                                boost(path = Movies::imdb / MovieImdb::rating, undefined = 3.0)
                            }
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::plot
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndPlotDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples-1">Constant Example</a>
     */
    fun findTitleAndPlotWithConstant(): AggregationResults<TitleAndPlotDto> {
        val aggregation = aggregation {
            search {
                compound {
                    should {
                        text {
                            query("tower")
                            path(Movies::plot)
                        }
                        text {
                            query("tower")
                            path(Movies::title)
                            score {
                                constant(5.0)
                            }
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::plot
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndPlotDto>(aggregation)
    }

    data class TitleDto(val title: String, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples-3">Arithmetic Example</a>
     */
    fun findTitleWithArithmetic(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("men")
                    score {
                        function {
                            expression = multiply(
                                path(value = Movies::imdb / MovieImdb::rating, undefined = 2.0),
                                score(),
                            )
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    data class Imdb(val rating: Double)
    data class TitleAndImdbDto(val title: String, val imdb: Imdb, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples-3">Gaussian Example</a>
     */
    fun findTitleWithGauss(): AggregationResults<TitleAndImdbDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("shop")
                    score {
                        function {
                            expression = gauss(
                                path = path(value = Movies::imdb / MovieImdb::rating, undefined = 4.6),
                                origin = 9.5,
                                scale = 5.0,
                                offset = 0.0,
                                decay = 0.5,
                            )
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                Movies::imdb..MovieImdb::rating alias "imdb.rating"
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndImdbDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples-3">Path Example</a>
     */
    fun findTitleWithPath(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("men")
                    score {
                        function {
                            expression = path(value = Movies::imdb / MovieImdb::rating, undefined = 4.6)
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#examples-3">Unary Example</a>
     */
    fun findTitleWithLog(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("men")
                    score {
                        function {
                            expression = log(
                                path(value = Movies::imdb / MovieImdb::rating, undefined = 10.0),
                            )
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }
}
