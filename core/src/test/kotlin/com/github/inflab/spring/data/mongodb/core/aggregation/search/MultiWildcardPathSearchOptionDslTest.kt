package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.bson.Document

internal class MultiWildcardPathSearchOptionDslTest : FreeSpec({
    fun path(block: MultiWildcardPathSearchOptionDsl<String>.() -> Unit) =
        MultiWildcardPathSearchOptionDsl<String>().apply(block)

    "multi" - {
        "should add single string path" {
            // given
            val option = path {
                "path" multi "analyzer"
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("value", "path").append("multi", "analyzer")
        }

        "should add multiple string path" {
            // given
            val option = path {
                "path1" multi "analyzer1"
                "path2" multi "analyzer2"
            }

            // when
            val result = option.build()

            // then
            result shouldBe listOf(
                Document("value", "path1").append("multi", "analyzer1"),
                Document("value", "path2").append("multi", "analyzer2"),
            )
        }

        "should add path by string property" {
            // given
            data class Test(val name: String?)
            val option = path {
                Test::name multi "analyzer"
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("value", "name").append("multi", "analyzer")
        }

        "should add path by string property in iterable" {
            // given
            data class Test(val names: Iterable<String?>?)
            val option = path {
                Test::names multi "analyzer"
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("value", "names").append("multi", "analyzer")
        }

        "should add path by nested property" {
            // given
            data class Child(val name: String?)
            data class Parent(val child: Child?)
            val option = path {
                (Parent::child..Child::name) multi "analyzer"
            }

            // when
            val result = option.build()

            // then
            result shouldBe Document("value", "child.name").append("multi", "analyzer")
        }
    }
})
