package com.github.inflab.example.spring.data.mongodb.repository.expression

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.stereotype.Repository

@Repository
class LiteralExpressionRepository(
    private val mongoTemplate: MongoTemplate,
) {
    @Document("storeInventory")
    data class StoreInventory(@Id val id: Long, val item: String, val price: String)

    data class CostsOneDollarDto(val id: Long, val costsOneDollar: Boolean)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/literal/#treat---as-a-literal">Treat $ as a literal</a>
     */
    fun findCostsOneDollar(): AggregationResults<CostsOneDollarDto> {
        val aggregation = aggregation {
            project {
                "costsOneDollar" expression {
                    // TODO: add $eq operator
                    ComparisonOperators.Eq.valueOf(StoreInventory::price.name).equalTo(literal("$1"))
                }
            }
        }

        return mongoTemplate.aggregate<StoreInventory, CostsOneDollarDto>(aggregation)
    }

    @Document("books")
    data class Book(@Id val id: Long, val title: String, val condition: String)

    data class EditionNumberValueDto(val id: Long, val title: String, val editionNumber: Int)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/literal/#treat---as-a-literal">Treat $ as a literal</a>
     */
    fun addEditionNumberValue(): AggregationResults<EditionNumberValueDto> {
        val aggregation = aggregation {
            project {
                +Book::title
                "editionNumber" expression {
                    literal(1)
                }
            }
        }

        return mongoTemplate.aggregate<Book, EditionNumberValueDto>(aggregation)
    }
}
