package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchOperation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation

internal class AggregationDslTest : FreeSpec({

    "count" - {
        "should create count stage with field name" {
            val aggregation = aggregation {
                count("fieldName")
            }

            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.count().`as`("fieldName"),
            ).toString()
        }
    }

    "search" - {
        "should create search stage with index option" {
            val aggregation = aggregation {
                search {
                    index = "indexName"
                }
            }

            aggregation.toString() shouldBe Aggregation.newAggregation(
                SearchOperation(Document("index", "indexName")),
            ).toString()
        }
    }
})
