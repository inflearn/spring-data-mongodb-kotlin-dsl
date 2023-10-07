package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class PathSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Warehouse(val inventory: Int, val color: String)

    @Document("cars")
    data class Car(
        val id: Long,
        val type: String,
        val make: String,
        val description: String,
        val warehouse: List<Warehouse>?,
    )

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#single-field-search">Single Field Search</a>
     */
    fun findBySingleField(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                text {
                    query("Ford")
                    path { +Car::make }
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#multiple-field-search">Multiple Field Search</a>
     */
    fun findByMultipleField(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                text {
                    query("blue")
                    path {
                        +Car::make
                        +Car::description
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#simple-analyzer-example">Simple Analyzer Example</a>
     */
    fun findBySimpleAnalyzer(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                text {
                    query("driver")
                    path {
                        Car::description multi "simpleAnalyzer"
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#whitespace-analyzer-example">Whitespace Analyzer Example</a>
     */
    fun findByWhitespaceAnalyzer(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                text {
                    query("Three")
                    path {
                        Car::description multi "simpleAnalyzer"
                    }
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#all-fields-search-example">All Fields Search Example</a>
     */
    fun findByWildcard(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                phrase {
                    path { wildcard() }
                    query("red")
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#nested-field-search-example">Nested Field Search Example</a>
     */
    fun findByNestedWildcard(): AggregationResults<Car> {
        val aggregation = aggregation {
            search {
                text {
                    path { Car::warehouse.ofWildcard() }
                    query("red")
                }
            }
        }

        return mongoTemplate.aggregate<Car, Car>(aggregation)
    }
}
