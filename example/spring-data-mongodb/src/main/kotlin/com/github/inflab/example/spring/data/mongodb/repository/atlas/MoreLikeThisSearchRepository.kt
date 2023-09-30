package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.Document
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class MoreLikeThisSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class TitleAndReleasedAndGenres(val title: String, val released: LocalDateTime?, val genres: List<String>?)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/morelikethis/#example-1--single-document-with-multiple-fields">Single Document with Multiple Fields</a>
     */
    fun findTitleAndGenres(): AggregationResults<TitleAndReleasedAndGenres> {
        val aggregation = aggregation {
            search {
                moreLikeThis {
                    like(
                        Document(
                            mapOf(
                                "title" to "The Godfather",
                                "genres" to "action",
                            ),
                        ),
                    )
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::released
                +Movies::genres
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndReleasedAndGenres>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/morelikethis/#example-2--input-document-excluded-in-results">Input Document Excluded in Results</a>
     */
    fun findByMovie(): AggregationResults<TitleAndReleasedAndGenres> {
        val movie = Document(
            mapOf(
                "_id" to ObjectId("573a1396f29313caabce4a9a"),
                "genres" to listOf("Crime", "Drama"),
                "title" to "The Godfather",
            ),
        )
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        moreLikeThis {
                            like(movie)
                        }
                    }

                    mustNot {
                        equal {
                            path("_id")
                            value(ObjectId("573a1396f29313caabce4a9a"))
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                excludeId()
                +Movies::title
                +Movies::released
                +Movies::genres
            }
        }

        return mongoTemplate.aggregate<Movies, TitleAndReleasedAndGenres>(aggregation)
    }

    data class IdAndTitleAndGenres(val id: String, val title: String, val genres: List<String>?)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/morelikethis/#example-3--multiple-analyzers">Multiple Analyzers</a>
     */
    fun findByMovies(): AggregationResults<IdAndTitleAndGenres> {
        val movies = listOf(
            mapOf(
                "_id" to ObjectId("573a1394f29313caabcde9ef"),
                "plot" to "Alice stumbles into the world of Wonderland. Will she get home? Not if the Queen of Hearts has her way.",
                "title" to "Alice in Wonderland",
            ),
            mapOf(
                "_id" to ObjectId("573a1398f29313caabce963d"),
                "plot" to "Alice is in Looking Glass land, where she meets many Looking Glass creatures and attempts to avoid the Jabberwocky, a monster that appears due to her being afraid.",
                "title" to "Alice in Wonderland",
            ),
            mapOf(
                "_id" to ObjectId("573a1398f29313caabce9644"),
                "plot" to "Alice is in Looking Glass land, where she meets many Looking Glass creatures and attempts to avoid the Jabberwocky, a monster that appears due to her being afraid.",
                "title" to "Alice in Wonderland",
            ),
            mapOf(
                "_id" to ObjectId("573a139df29313caabcfb504"),
                "plot" to "The wizards behind The Odyssey (1997) and Merlin (1998) combine Lewis Carroll's \"Alice in Wonderland\" and \"Through the Looking Glass\" into a two-hour special that just gets curiouser and curiouser.",
                "title" to "Alice in Wonderland",
            ),
            mapOf(
                "_id" to ObjectId("573a13bdf29313caabd5933b"),
                "plot" to "Nineteen-year-old Alice returns to the magical world from her childhood adventure, where she reunites with her old friends and learns of her true destiny: to end the Red Queen's reign of terror.",
                "title" to "Alice in Wonderland",
            ),
        ).map(::Document)
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        moreLikeThis {
                            like(movies)
                        }
                    }

                    mustNot {
                        equal {
                            path("_id")
                            value(ObjectId("573a1394f29313caabcde9ef"))
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                +Movies::title
                +Movies::genres
            }
        }

        return mongoTemplate.aggregate<Movies, IdAndTitleAndGenres>(aggregation)
    }
}
