package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchMetaOperation
import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchOperation
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.StringOperators

internal class AggregationDslTest : FreeSpec({

    "stage" - {
        "should add a raw stage" {
            // when
            val aggregation = aggregation {
                stage(Aggregation.count().`as`("test"))
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.count().`as`("test"),
            ).toString()
        }
    }

    "option" - {
        "should add an options" {
            // when
            val aggregation = aggregation {
                options(
                    allowDiskUse = true,
                    explain = true,
                )
                count("fieldName")
            }

            // then
            aggregation.options.isAllowDiskUse.shouldBeTrue()
            aggregation.options.isExplain.shouldBeTrue()
            aggregation.options.isSkipResults.shouldBeFalse()
        }
    }

    "count" - {
        "should create count stage with field name" {
            // when
            val aggregation = aggregation {
                count("fieldName")
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.count().`as`("fieldName"),
            ).toString()
        }
    }

    "search" - {
        "should create search stage with index option" {
            // when
            val aggregation = aggregation {
                search {
                    index = "indexName"
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                SearchOperation(Document("index", "indexName")),
            ).toString()
        }
    }

    "searchMeta" - {
        "should create searchMeta stage with index option" {
            // when
            val aggregation = aggregation {
                searchMeta {
                    index = "indexName"
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                SearchMetaOperation(Document("index", "indexName")),
            ).toString()
        }
    }

    "project" - {
        "should create project stage with field name" {
            // when
            val aggregation = aggregation {
                project {
                    +"fieldName"
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.project("fieldName"),
            ).toString()
        }
    }

    "sort" - {
        "should create sort stage with field name" {
            // when
            val aggregation = aggregation {
                sort {
                    "fieldName" by Ascending
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                ExtendedSortOperation().apply { ascending("fieldName") },
            ).toString()
        }
    }

    "sortByCount" - {
        "should create sortByCount stage with expression" {
            // when
            val expression = StringOperators.valueOf("fieldName").ltrim()
            val aggregation = aggregation {
                sortByCount(expression)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.sortByCount(expression),
            ).toString()
        }
    }
})
