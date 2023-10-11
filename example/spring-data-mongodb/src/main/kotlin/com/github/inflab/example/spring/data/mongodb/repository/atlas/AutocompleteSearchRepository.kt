package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.aggregation.search.AutocompleteTokenOrder
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class AutocompleteSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleDto(val title: String)

    data class TitleWithPlotDto(val title: String, val plot: String)

    data class Bucket(val id: String, val count: Long)
    data class Count(val lowerBound: Long)
    data class Facet(val buckets: List<Bucket>)
    data class TitleFacet(val titleFacet: Facet)
    data class StringFacetDto(val count: Count, val facet: TitleFacet)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#basic-example">Basic Example</a>
     */
    fun findByTitle(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("off")
                }
            }

            limit(10)

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#fuzzy-example">Fuzzy Example</a>
     */
    fun findByTitleWithFuzzy(): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                autocomplete {
                    path(Movies::title)
                    query("pre")
                    fuzzy(
                        maxEdits = 1,
                        prefixLength = 2,
                        maxExpansions = 256,
                    )
                }
            }

            limit(10)

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#token-order-example">Token Order Example</a>
     */
    fun findByTitleWithTokenOrder(autocompleteTokenOrder: AutocompleteTokenOrder): AggregationResults<TitleDto> {
        val aggregation = aggregation {
            search {
                autocomplete {
                    path(Movies::title)
                    query("men with")
                    tokenOrder(autocompleteTokenOrder)
                }
            }

            limit(4)

            project {
                excludeId()
                +Movies::title
            }
        }

        return mongoTemplate.aggregate<Movies, TitleDto>(aggregation)
    }

    // TODO: add Highlighting Example
    // https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#highlighting-example

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#search-across-multiple-fields">Search Across Multiple Fields</a>
     */
    fun findByTitleWithCompound(): AggregationResults<TitleWithPlotDto> {
        val aggregation = aggregation {
            search {
                compound {
                    should {
                        autocomplete {
                            query("inter")
                            path(Movies::title)
                        }
                        text {
                            query("inter")
                            path(Movies::plot)
                        }
                    }
                    minimumShouldMatch = 1
                }
            }

            limit(10)

            project {
                excludeId()
                +Movies::title
                +Movies::plot
            }
        }

        return mongoTemplate.aggregate<Movies, TitleWithPlotDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete/#bucket-results-through-facet-queries">Bucket Results Through Facet Queries</a>
     */
    fun findByTitleWithBucketResults(): AggregationResults<StringFacetDto> {
        val aggregation = aggregation {
            search {
                facet {
                    operator {
                        autocomplete {
                            query("Gravity")
                            path(Movies::title)
                        }
                    }
                    "titleFacet".stringFacet {
                        path(Movies::title)
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Movies, StringFacetDto>(aggregation)
    }
}
