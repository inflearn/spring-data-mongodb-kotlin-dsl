package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class WildcardPathSearchOptionDslTest : FreeSpec({
    fun path(block: WildcardPathSearchOptionDsl<String>.() -> Unit) =
        WildcardPathSearchOptionDsl<String>().apply(block)

    "wildcard" - {
        "should add single wildcard character" {
            // given
            val option = path {
                wildcard()
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("wildcard", "*")
        }

        "should add single string path" {
            // given
            val option = path {
                wildcard("path*")
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("wildcard", "path*")
        }

        "should add multiple string path" {
            // given
            val option = path {
                wildcard("path1*")
                "path2".ofWildcard()
            }

            // when
            val result = option.build()

            // then
            result shouldBe listOf(Document("wildcard", "path1*"), Document("wildcard", "path2*"))
        }

        "should add path by string property" {
            // given
            data class Test(val name: String?)
            val option = path {
                Test::name.ofWildcard()
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("wildcard", "name.*")
        }

        "should add path by string property in iterable" {
            // given
            data class Test(val names: Iterable<String?>?)
            val option = path {
                Test::names.ofWildcard()
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("wildcard", "names.*")
        }

        "should add path by nested property" {
            // given
            data class Child(val name: String?)
            data class Parent(val child: Child?)
            val option = path {
                (Parent::child..Child::name).ofWildcard()
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("wildcard", "child.name.*")
        }
    }
})
