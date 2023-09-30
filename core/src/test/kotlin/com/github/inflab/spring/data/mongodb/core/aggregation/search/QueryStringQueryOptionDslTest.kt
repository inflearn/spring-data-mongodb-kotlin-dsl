package com.github.inflab.spring.data.mongodb.core.aggregation.search

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class QueryStringQueryOptionDslTest : FreeSpec({
    fun query(block: QueryStringQueryOptionDsl.() -> Unit) =
        QueryStringQueryOptionDsl().apply(block)

    "text" - {
        "should add a text" {
            // given
            val option = query {
                query = text("search")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "\"search\""
        }

        "should add a text with field" {
            // given
            val option = query {
                query = text("search", "field")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "field:\"search\""
        }

        "should escape special characters" {
            // given
            val option = query {
                query = text("c?u*t")
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
                query = wildcard("search$WILDCARD$QUESTION")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "search*?"
        }

        "should add a wildcard with field" {
            // given
            val option = query {
                query = wildcard("search", "field")
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
                query = regex("search")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "/search/"
        }

        "should add a regex with field" {
            // given
            val option = query {
                query = regex("search", "field")
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
                query = range("left", "right", leftInclusion = true, rightInclusion = true)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "[\"left\" TO \"right\"]"
        }

        "should add a exclusive range" {
            // given
            val option = query {
                query = range("left", "right", leftInclusion = false, rightInclusion = false)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "{\"left\" TO \"right\"}"
        }

        "should add a half-open range" {
            // given
            val option = query {
                query = range("left", "right", leftInclusion = true, rightInclusion = false)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "[\"left\" TO \"right\"}"
        }

        "should not add double quote if wildcard is given" {
            // given
            val option = query {
                query = range(WILDCARD, WILDCARD)
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
                query = fuzzy("search", 2)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "search~2"
        }

        "should add a fuzzy with field" {
            // given
            val option = query {
                query = fuzzy("search", 3, "field")
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
                query = not(text("search"))
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
                query = text("search1") and text("search2")
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
                query = text("search1") or text("search2")
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
                query = sub(text("a") or text("b")) and text("c")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "(\"a\" OR \"b\") AND \"c\""
        }

        "should add subquery block with field" {
            // given
            val option = query {
                query = sub(text("a") or text("b"), "c")
            }

            // when
            val result = option.build()

            // then
            result shouldBe "c:(\"a\" OR \"b\")"
        }
    }
})
