package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.analytics.Customers
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class InSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/in/#examples">Compound Query Match</a>
     */
    fun findScoreByCompound(): AggregationResults<NameAndScores> {
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        text {
                            path("name")
                            query("James")
                        }
                    }
                    should {
                        `in` {
                            path("_id")
                            value(
                                ObjectId("5ca4bbcea2dd94ee58162a72"),
                                ObjectId("5ca4bbcea2dd94ee58162a91"),
                            )
                        }
                    }
                }
            }

            // TODO: add $limit stage

            project {
                +Customers::name
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Customers, NameAndScores>(aggregation)
    }

    data class NameAndScores(val id: String, val name: String, val score: Double)
}
