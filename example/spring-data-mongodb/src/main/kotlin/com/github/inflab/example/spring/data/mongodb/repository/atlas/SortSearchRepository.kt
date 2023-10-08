package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.MovieAwards
import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.ArrayElemAt
import org.springframework.data.mongodb.core.aggregation.ReplaceWithOperation
import org.springframework.data.mongodb.core.aggregation.SetOperation
import org.springframework.data.mongodb.core.aggregation.SystemVariable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class SortSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndReleasedDto(val title: String, val released: LocalDateTime, val score: Double?)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#date-search-and-sort">Date Search and Sort</a>
     */
    fun sortByDate(): AggregationResults<TitleAndReleasedDto> {
        val aggregation = aggregation {
            search {
                range {
                    path(Movies::released)
                    gt(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                    lt(LocalDateTime.of(2015, 1, 1, 0, 0, 0))
                }
                sort {
                    Movies::released by desc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
                +Movies::released
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndReleasedDto>(aggregation)
    }

    data class Awards(val wins: Int)
    data class TitleAndAwardsDto(val title: String, val awards: Awards)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#number-search-and-sort">Number Search and Sort</a>
     */
    fun sortByNumber(): AggregationResults<TitleAndAwardsDto> {
        val aggregation = aggregation {
            search {
                range {
                    path(Movies::awards..MovieAwards::wins)
                    gt(3)
                }
                sort {
                    Movies::awards..MovieAwards::wins by desc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
                Movies::awards..MovieAwards::wins alias "awards.wins"
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndAwardsDto>(aggregation)
    }

    data class TitleAndScoreDto(val title: String, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#string-search-and-sort">String Search and Sort</a>
     */
    fun sortByString(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("country")
                }
                sort {
                    Movies::title by asc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }

    data class SortByCompoundDto(val title: String, val released: LocalDateTime, val wins: Int, val score: Double)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#compound-search-and-sort">Compound Search and Sort</a>
     */
    fun sortByCompound(): AggregationResults<SortByCompoundDto> {
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        text {
                            path(Movies::title)
                            query("dance")
                        }
                    }
                    should {
                        range {
                            path(Movies::awards..MovieAwards::wins)
                            gte(2)
                        }
                        range {
                            path(Movies::released)
                            gte(LocalDateTime.of(1990, 1, 1, 0, 0, 0))
                        }
                    }
                }
                sort {
                    Movies::awards..MovieAwards::wins by desc
                    Movies::title by asc
                    Movies::released by desc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(10))

            project {
                excludeId()
                +Movies::title
                +Movies::released
                +(Movies::awards..MovieAwards::wins)
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, SortByCompoundDto>(aggregation)
    }

    data class AwardsBucket(val id: Int, val count: Long)
    data class ReleasedBucket(val id: LocalDateTime, val count: Long)
    data class AwardsFacet(val buckets: List<AwardsBucket>)
    data class ReleasedFacet(val buckets: List<ReleasedBucket>)
    data class Count(val lowerBound: Long)
    data class Facet(val releasedFacet: ReleasedFacet, val awardsFacet: AwardsFacet)
    data class MetaFacet(val count: Count, val facet: Facet)
    data class SortByFacetDto(val docs: List<TitleAndReleasedDto>, val meta: MetaFacet)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#facet-search-and-sort">Facet Search and Sort</a>
     */
    fun sortByFacet(): AggregationResults<SortByFacetDto> {
        val aggregation = aggregation {
            search {
                facet {
                    operator {
                        range {
                            path(Movies::released)
                            gt(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                            lt(LocalDateTime.of(2015, 1, 1, 0, 0, 0))
                        }
                    }
                    "awardsFacet" numericFacet {
                        path(Movies::awards..MovieAwards::wins)
                        boundaries(1, 5, 10, 15)
                    }
                    "releasedFacet" dateFacet {
                        path(Movies::released)
                        boundaries(
                            LocalDateTime.of(2010, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2011, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2012, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2013, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2014, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2015, 1, 1, 0, 0, 0),
                        )
                    }
                }
                sort {
                    Movies::released by desc
                }
            }

            // TODO: add $facet stage
            stage(
                Aggregation.facet()
                    .and(
                        Aggregation.limit(5),
                        Aggregation.project()
                            .andExclude("_id")
                            .andInclude("title", "released", "awards.wins"),
                    )
                    .`as`("docs")
                    .and(
                        ReplaceWithOperation.replaceWithValueOf(SystemVariable.SEARCH_META),
                        Aggregation.limit(1),
                    )
                    .`as`("meta"),
            )

            // TODO: add $set stage
            stage(
                SetOperation.set("meta")
                    .toValueOf(ArrayElemAt.arrayOf("\$meta").elementAt(0)),
            )
        }

        return mongoTemplate.aggregate<Movies, SortByFacetDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#sort-by-score">Sort by Score Ascending</a>
     */
    fun sortByScoreAscending(): AggregationResults<TitleAndScoreDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("story")
                }
                sort {
                    score by asc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndScoreDto>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort/#sort-by-score">Sort by Score Unique</a>
     */
    fun sortByScoreUnique(): AggregationResults<TitleAndReleasedDto> {
        val aggregation = aggregation {
            search {
                text {
                    path(Movies::title)
                    query("prince")
                }
                sort {
                    score by desc
                    Movies::released by asc
                }
            }

            // TODO: add $limit stage
            stage(Aggregation.limit(5))

            project {
                excludeId()
                +Movies::title
                +Movies::released
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndReleasedDto>(aggregation)
    }
}
