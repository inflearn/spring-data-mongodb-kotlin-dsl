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
import org.springframework.data.mongodb.core.aggregation.UnionWithOperation
import org.springframework.data.mongodb.core.aggregation.UnsetOperation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.TextCriteria

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
                    "fieldName" by asc
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                ExtendedSortOperation().apply { ascending("fieldName") },
            ).toString()
        }
    }

    "sortByCount" - {
        "should create sortByCount stage with string" {
            // when
            val aggregation = aggregation {
                sortByCount("fieldName")
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.sortByCount("fieldName"),
            ).toString()
        }

        "should create sortByCount stage with property" {
            // when
            data class Test(val fieldName: String)
            val aggregation = aggregation {
                sortByCount(Test::fieldName)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.sortByCount("fieldName"),
            ).toString()
        }

        "should create sortByCount stage with expression" {
            // when
            val expression = StringOperators.valueOf("fieldName").ltrim()
            val aggregation = aggregation {
                sortByCount { expression }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.sortByCount(expression),
            ).toString()
        }
    }

    "match" - {
        "should create match stage with criteria" {
            // when
            val criteria = Criteria.where("fieldName")
            val aggregation = aggregation {
                match(criteria)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.match(criteria),
            ).toString()
        }

        "should create match stage with criteria definition" {
            // when
            val criteria = TextCriteria.forDefaultLanguage().matching("operating")
            val aggregation = aggregation {
                match(criteria)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.match(criteria),
            ).toString()
        }

        "should create match stage with aggregation expression" {
            // when
            val expression = StringOperators.valueOf("fieldName").ltrim()
            val aggregation = aggregation {
                match { expression }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.match(expression),
            ).toString()
        }
    }

    "unwind" - {
        "should create unwind stage" {
            // when
            val aggregation = aggregation {
                unwind {
                    path("fieldName")
                    includeArrayIndex = "index"
                    preserveNullAndEmptyArrays = true
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.unwind("fieldName", "index", true),
            ).toString()
        }
    }

    "lookup" - {
        "should create lookup stage" {
            // when
            val aggregation = aggregation {
                lookup {
                    from("from")
                    localField("localField")
                    foreignField("foreignField")
                    `as`("as")
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                ExtendedLookupOperation().apply {
                    from("from")
                    localField("localField")
                    foreignField("foreignField")
                    setAs("as")
                },
            ).toString()
        }
    }

    "unset" - {
        "should create unset stage" {
            // given
            data class Test(val single: String?, val multiple: List<Int?>?)

            // when
            val aggregation = aggregation {
                unset {
                    +"fieldName"
                    +Test::single
                    +Test::multiple
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                UnsetOperation.unset("fieldName", "single", "multiple"),
            ).toString()
        }
    }

    "unionWith" - {
        "should create unionWith stage" {
            // when
            val aggregation = aggregation {
                unionWith {
                    coll("collectionName")
                }
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                UnionWithOperation.unionWith("collectionName"),
            ).toString()
        }
    }

    "skip" - {
        "should create skip stage" {
            // when
            val aggregation = aggregation {
                skip(10L)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.skip(10L),
            ).toString()
        }
    }

    "limit" - {
        "should create limit stage" {
            // when
            val aggregation = aggregation {
                limit(10L)
            }

            // then
            aggregation.toString() shouldBe Aggregation.newAggregation(
                Aggregation.limit(10L),
            ).toString()
        }
    }
})
