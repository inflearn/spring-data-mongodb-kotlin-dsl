package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.mflix.Movies
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
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
                        Document("title", "The Godfather").append("genres", "action"),
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
        val movie = Document("_id", ObjectId("573a1396f29313caabce4a9a"))
            .append("genres", listOf("Crime", "Drama"))
            .append("title", "The Godfather")
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
            Document("_id", ObjectId("573a1394f29313caabcde9ef"))
                .append("plot", "Alice stumbles into the world of Wonderland. Will she get home? Not if the Queen of Hearts has her way.")
                .append("title", "Alice in Wonderland"),
            Document("_id", ObjectId("573a1398f29313caabce963d"))
                .append("plot", "Alice is in Looking Glass land, where she meets many Looking Glass creatures and attempts to avoid the Jabberwocky, a monster that appears due to her being afraid.")
                .append("title", "Alice in Wonderland"),
            Document("_id", ObjectId("573a1398f29313caabce9644"))
                .append("plot", "Alice is in Looking Glass land, where she meets many Looking Glass creatures and attempts to avoid the Jabberwocky, a monster that appears due to her being afraid.")
                .append("title", "Alice in Wonderland"),
            Document("_id", ObjectId("573a139df29313caabcfb504"))
                .append("plot", "The wizards behind The Odyssey (1997) and Merlin (1998) combine Lewis Carroll's \"Alice in Wonderland\" and \"Through the Looking Glass\" into a two-hour special that just gets curiouser and curiouser.")
                .append("title", "Alice in Wonderland"),
            Document("_id", ObjectId("573a13bdf29313caabd5933b"))
                .append("plot", "Nineteen-year-old Alice returns to the magical world from her childhood adventure, where she reunites with her old friends and learns of her true destiny: to end the Red Queen's reign of terror.")
                .append("title", "Alice in Wonderland"),
        )
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        moreLikeThis {
                            // change list of movie to vararg
                            like(*movies.toTypedArray())
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
