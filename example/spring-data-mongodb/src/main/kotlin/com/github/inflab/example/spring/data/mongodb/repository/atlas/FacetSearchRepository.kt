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
class FacetSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Bucket(val id: String, val count: Long)
    data class Count(val lowerBound: Long)
    data class Facet(val buckets: List<Bucket>)
    data class GenresFacet(val genresFacet: Facet)
    data class StringFacetDto(val count: Count, val facet: GenresFacet)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#example">String Facet Example</a>
     */
    fun findYearWithStringFacet(): AggregationResults<StringFacetDto> {
        val aggregation = aggregation {
            searchMeta {
                facet {
                    operator {
                        range {
                            path(Movies::year)
                            gte(2000)
                            lte(2015)
                        }
                    }
                    "genresFacet" stringFacet {
                        path(Movies::genres)
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Movies, StringFacetDto>(aggregation)
    }

    data class YearFacet(val yearFacet: Facet)
    data class NumericFacetDto(val count: Count, val facet: YearFacet)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#example-1">Numeric Facet Example</a>
     */
    fun findYearWithNumberFacet(): AggregationResults<NumericFacetDto> {
        val aggregation = aggregation {
            searchMeta {
                facet {
                    operator {
                        range {
                            path(Movies::year)
                            gte(1980)
                            lte(2000)
                        }
                    }
                    "yearFacet" numericFacet {
                        path(Movies::year)
                        boundaries(1980, 1990, 2000)
                        default("other")
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Movies, NumericFacetDto>(aggregation)
    }

    data class DateBucket(val id: Any, val count: Long)
    data class DateFacet(val buckets: List<DateBucket>)
    data class ReleaseFacet(val releaseFacet: DateFacet)
    data class DateFacetDto(val count: Count, val facet: ReleaseFacet)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#example-2">Date Facet Example</a>
     */
    fun findReleasedWithDateFacet(): AggregationResults<DateFacetDto> {
        val aggregation = aggregation {
            searchMeta {
                facet {
                    operator {
                        range {
                            path(Movies::released)
                            gte(LocalDateTime.of(2000, 1, 1, 0, 0, 0))
                            lte(LocalDateTime.of(2015, 1, 31, 0, 0, 0))
                        }
                    }
                    "releaseFacet" dateFacet {
                        path(Movies::released)
                        boundaries(
                            LocalDate.of(2000, 1, 1),
                            LocalDate.of(2005, 1, 1),
                            LocalDate.of(2010, 1, 1),
                            LocalDate.of(2015, 1, 1),
                        )
                        default("other")
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Movies, DateFacetDto>(aggregation)
    }
}
