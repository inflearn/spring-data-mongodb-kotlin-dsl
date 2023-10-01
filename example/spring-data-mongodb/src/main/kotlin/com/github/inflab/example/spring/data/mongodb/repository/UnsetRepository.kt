package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.stereotype.Repository

@Repository
class UnsetRepository(
    private val mongoTemplate: MongoTemplate,
) {

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unset/#remove-a-single-field">Remove a Single Field</a>
     */
    fun unsetCopies(): AggregationResults<Document> {
        val aggregation = aggregation {
            unset {
                +"copies"
            }
        }

        return mongoTemplate.aggregate(aggregation, BOOKS, Document::class.java)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unset/#remove-top-level-fields">Remove Top-Level Fields</a>
     */
    fun unsetIsbnAndCopies(): AggregationResults<Document> {
        val aggregation = aggregation {
            unset {
                +"isbn"
                +"copies"
            }
        }

        return mongoTemplate.aggregate(aggregation, BOOKS, Document::class.java)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unset/#remove-embedded-fields">Remove Embedded Fields</a>
     */
    fun unsetEmbeddedFields(): AggregationResults<Document> {
        val aggregation = aggregation {
            unset {
                +"isbn"
                +"author.first"
                +"copies.warehouse"
            }
        }

        return mongoTemplate.aggregate(aggregation, BOOKS, Document::class.java)
    }

    companion object {
        const val BOOKS = "books"
    }
}
