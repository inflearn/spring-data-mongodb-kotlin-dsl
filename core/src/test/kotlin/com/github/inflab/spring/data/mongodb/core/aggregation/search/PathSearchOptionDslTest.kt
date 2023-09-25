package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

internal class PathSearchOptionDslTest : FreeSpec({
    fun path(block: PathSearchOptionDsl<String>.() -> Unit) =
        PathSearchOptionDsl<String>().apply(block)

    "unaryPlus" - {
        "should add single string path" {
            // given
            val option = path {
                +"path"
            }

            // when
            val result = option.build()

            // then
            result shouldBe "path"
        }

        "should add single property path" {
            // given
            data class Test(val name: String)
            val option = path {
                +(Test::name)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "name"
        }

        "should add single iterable property path" {
            // given
            data class Test(val names: List<String>)
            val option = path {
                +(Test::names)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "names"
        }

        "should add multiple string path" {
            // given
            val option = path {
                +"path1"
                +"path2"
            }

            // when
            val result = option.build()

            // then
            result shouldBe listOf("path1", "path2")
        }

        "should add nested list property" {
            // given
            data class GrandChild(val names: List<String>)
            data class Child(val items: List<GrandChild>)
            data class Parent(val child: List<Child>)
            val option = path {
                +(Parent::child..Child::items..GrandChild::names)
            }

            // when
            val result = option.build()

            // then
            result shouldBe "child.items.names"
        }
    }
})
