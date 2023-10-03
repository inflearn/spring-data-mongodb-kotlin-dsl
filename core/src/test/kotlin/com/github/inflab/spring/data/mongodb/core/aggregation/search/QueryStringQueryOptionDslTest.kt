package com.github.inflab.spring.data.mongodb.core.aggregation.search

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class QueryStringQueryOptionDslTest : FreeSpec({
    data class Sample(val field: String)

    fun query(block: QueryStringQueryOptionDsl.() -> QueryStringQueryOptionDsl.Query): String {
        val query = QueryStringQueryOptionDsl().block()

        return query.toString()
    }

    "text" - {
        "should add a text" {
            // given, when
            val result = query {
                text("search")
            }

            // then
            result shouldBe "\"search\""
        }

        "should add a text with string field" {
            // given, when 
            val result = query {
                text("search", "field")
            }

            // then
            result shouldBe "field:\"search\""
        }

        "should add a text with property field" {
            // given, when 
            val result = query {
                text("search", Sample::field)
            }

            // then
            result shouldBe "field:\"search\""
        }

        "should escape special characters" {
            // given, when 
            val result = query {
                text("c?u*t")
            }

            // then
            result shouldBe "\"c\\?u\\*t\""
        }
    }

    "wildcard" - {
        "should add a wildcard" {
            // given, when 
            val result = query {
                wildcard("search$WILDCARD$QUESTION")
            }

            // then
            result shouldBe "search*?"
        }

        "should add a wildcard with string field" {
            // given, when 
            val result = query {
                wildcard("search", "field")
            }

            // then
            result shouldBe "field:search"
        }

        "should add a wildcard with property field" {
            // given, when 
            val result = query {
                wildcard("search", Sample::field)
            }

            // then
            result shouldBe "field:search"
        }
    }

    "regex" - {
        "should add a regex" {
            // given, when 
            val result = query {
                regex("search")
            }

            // then
            result shouldBe "/search/"
        }

        "should add a regex with string field" {
            // given, when 
            val result = query {
                regex("search", "field")
            }

            // then
            result shouldBe "field:/search/"
        }

        "should add a regex with property field" {
            // given, when 
            val result = query {
                regex("search", Sample::field)
            }

            // then
            result shouldBe "field:/search/"
        }
    }

    "range" - {
        "should add a inclusive range" {
            // given, when 
            val result = query {
                range("left", "right", leftInclusion = true, rightInclusion = true)
            }

            // then
            result shouldBe "[\"left\" TO \"right\"]"
        }

        "should add a exclusive range" {
            // given, when 
            val result = query {
                range("left", "right", leftInclusion = false, rightInclusion = false)
            }

            // then
            result shouldBe "{\"left\" TO \"right\"}"
        }

        "should add a half-open range" {
            // given, when 
            val result = query {
                range("left", "right", leftInclusion = true, rightInclusion = false)
            }

            // then
            result shouldBe "[\"left\" TO \"right\"}"
        }

        "should not add double quote if wildcard is given" {
            // given, when 
            val result = query {
                range(WILDCARD, WILDCARD)
            }

            // then
            result shouldBe "[* TO *]"
        }
    }

    "fuzzy" - {
        "should add a fuzzy" {
            // given, when 
            val result = query {
                fuzzy("search", 2)
            }

            // then
            result shouldBe "search~2"
        }

        "should add a fuzzy with string field" {
            // given, when 
            val result = query {
                fuzzy("search", 3, "field")
            }

            // then
            result shouldBe "field:search~3"
        }

        "should add a fuzzy with property field" {
            // given, when 
            val result = query {
                fuzzy("search", 3, Sample::field)
            }

            // then
            result shouldBe "field:search~3"
        }
    }

    "not" - {
        "should add NOT block" {
            // given, when 
            val result = query {
                not(text("search"))
            }

            // then
            result shouldBe "NOT (\"search\")"
        }
    }

    "and" - {
        "should add AND block" {
            // given, when 
            val result = query {
                text("search1") and text("search2")
            }

            // then
            result shouldBe "\"search1\" AND \"search2\""
        }
    }

    "or" - {
        "should add OR block" {
            // given, when 
            val result = query {
                text("search1") or text("search2")
            }

            // then
            result shouldBe "\"search1\" OR \"search2\""
        }
    }

    "sub" - {
        "should add subquery block" {
            // given, when 
            val result = query {
                sub(text("a") or text("b")) and text("c")
            }

            // then
            result shouldBe "(\"a\" OR \"b\") AND \"c\""
        }

        "should add subquery block with string field" {
            // given, when 
            val result = query {
                sub(text("a") or text("b"), "field")
            }

            // then
            result shouldBe "field:(\"a\" OR \"b\")"
        }

        "should add subquery block with property field" {
            // given, when 
            val result = query {
                sub(text("a") or text("b"), Sample::field)
            }

            // then
            result shouldBe "field:(\"a\" OR \"b\")"
        }
    }
})
