package com.github.inflab.spring.data.mongodb.core.aggregation.search

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class QueryStringQueryOptionDslTest : FreeSpec({
    data class Sample(val field: String)

    fun query(block: QueryStringQueryOptionDsl.() -> Unit) =
        QueryStringQueryOptionDsl().apply(block)

    "text" - {
        "should add a text" {
            // given
            val option = query {
                text("search")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "\"search\""
        }

        "should add a text with string field" {
            // given
            val option = query {
                text("search", "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:\"search\""
        }

        "should add a text with property field" {
            // given
            val option = query {
                text("search", Sample::field)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:\"search\""
        }

        "should escape special characters" {
            // given
            val option = query {
                text("c?u*t")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "\"c\\?u\\*t\""
        }
    }

    "wildcard" - {
        "should add a wildcard" {
            // given
            val option = query {
                wildcard("search$WILDCARD$QUESTION")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "search*?"
        }

        "should add a wildcard with string field" {
            // given
            val option = query {
                wildcard("search", "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:search"
        }

        "should add a wildcard with property field" {
            // given
            val option = query {
                wildcard("search", Sample::field)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:search"
        }
    }

    "regex" - {
        "should add a regex" {
            // given
            val option = query {
                regex("search")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "/search/"
        }

        "should add a regex with string field" {
            // given
            val option = query {
                regex("search", "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:/search/"
        }

        "should add a regex with property field" {
            // given
            val option = query {
                regex("search", Sample::field)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:/search/"
        }
    }

    "range" - {
        "should add a inclusive range" {
            // given
            val option = query {
                range("left", "right", leftInclusion = true, rightInclusion = true)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "[\"left\" TO \"right\"]"
        }

        "should add a exclusive range" {
            // given
            val option = query {
                range("left", "right", leftInclusion = false, rightInclusion = false)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "{\"left\" TO \"right\"}"
        }

        "should add a half-open range" {
            // given
            val option = query {
                range("left", "right", leftInclusion = true, rightInclusion = false)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "[\"left\" TO \"right\"}"
        }

        "should not add double quote if wildcard is given" {
            // given
            val option = query {
                range(WILDCARD, WILDCARD)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "[* TO *]"
        }
    }

    "fuzzy" - {
        "should add a fuzzy" {
            // given
            val option = query {
                fuzzy("search", 2)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "search~2"
        }

        "should add a fuzzy with string field" {
            // given
            val option = query {
                fuzzy("search", 3, "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:search~3"
        }

        "should add a fuzzy with property field" {
            // given
            val option = query {
                fuzzy("search", 3, Sample::field)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:search~3"
        }
    }

    "not" - {
        "should add NOT block" {
            // given
            val option = query {
                not(text("search"))
            }

            // when
            val result = option.build()

            // then
            result shouldBe "NOT (\"search\")"
        }
    }

    "and" - {
        "should add AND block" {
            // given
            val option = query {
                text("search1") and text("search2")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "\"search1\" AND \"search2\""
        }
    }

    "or" - {
        "should add OR block" {
            // given
            val option = query {
                text("search1") or text("search2")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "\"search1\" OR \"search2\""
        }
    }

    "sub" - {
        "should add subquery block" {
            // given
            val option = query {
                sub(text("a") or text("b")) and text("c")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "(\"a\" OR \"b\") AND \"c\""
        }

        "should add subquery block with string field" {
            // given
            val option = query {
                sub(text("a") or text("b"), "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:(\"a\" OR \"b\")"
        }

        "should add subquery block with property field" {
            // given
            val option = query {
                sub(text("a") or text("b"), Sample::field)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:(\"a\" OR \"b\")"
        }
    }
})
