package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.example.spring.data.mongodb.entity.sample.Fruits
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class ExistsSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/exists/#compound-example">Compound Example</a>
     */
    fun findAppleByCompound(): AggregationResults<Fruits> {
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        exists {
                            path(Fruits::type)
                        }
                        text {
                            query("apple")
                            path(Fruits::type)
                        }
                    }
                    should {
                        text {
                            query("fuji")
                            path(Fruits::description)
                        }
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Fruits, Fruits>(aggregation)
    }
}
