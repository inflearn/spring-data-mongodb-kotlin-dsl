package com.github.inflab.example.spring.data.mongodb.repository.atlas

import com.github.inflab.example.spring.data.mongodb.entity.analytics.Customers
import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class InSearchRepository(
    private val mongoTemplate: MongoTemplate,
) {

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/in/#examples">Array Value Field Match</a>
     */
    fun findBirthDate(): AggregationResults<NameAndBirthDateDto> {
        val aggregation = aggregation {
            search {
                `in` {
                    path(Customers::birthdate)
                    value(
                        LocalDateTime.of(1977, 3, 2, 2, 20, 31),
                        LocalDateTime.of(1977, 3, 1, 0, 0, 0),
                        LocalDateTime.of(1977, 5, 6, 21, 57, 35),
                    )
                }
            }

            project {
                excludeId()
                +Customers::name
                +Customers::birthdate
            }
        }

        return mongoTemplate.aggregate<Customers, NameAndBirthDateDto>(aggregation)
    }

    data class NameAndBirthDateDto(val name: String, val birthdate: LocalDateTime)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/in/#examples">Array Value Field Match</a>
     */
    fun findAccountsByArray(): AggregationResults<NameAndAccountsDto> {
        val aggregation = aggregation {
            search {
                `in` {
                    path("accounts")
                    value(
                        371138,
                        371139,
                        371140,
                    )
                }
            }

            project {
                excludeId()
                +Customers::name
                +Customers::accounts
            }
        }

        return mongoTemplate.aggregate<Customers, NameAndAccountsDto>(aggregation)
    }

    data class NameAndAccountsDto(val name: String, val accounts: List<Int>)

    /**
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/in/#examples">Compound Query Match</a>
     */
    fun findScoreByCompound(): AggregationResults<NameAndScoresDto> {
        val aggregation = aggregation {
            search {
                compound {
                    must {
                        text {
                            path(Customers::name)
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

            limit(5)

            project {
                +Customers::name
                searchScore()
            }
        }

        return mongoTemplate.aggregate<Customers, NameAndScoresDto>(aggregation)
    }

    data class NameAndScoresDto(val id: String, val name: String, val score: Double)
}
