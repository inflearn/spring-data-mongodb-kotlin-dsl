package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.util.shouldBeJson
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import java.time.temporal.Temporal

internal class SortSearchOptionDslTest : FreeSpec({
    fun sort(block: SortSearchOptionDsl.() -> Unit) =
        SortSearchOptionDsl().apply(block)

    "should choose a name that does not conflict with all field names" {
        // given
        val option = sort {
            score by asc
            "unused0" by asc
            "unused1" by desc
            "unused2" by asc
            "unused3" by desc
            "unused4" by asc
        }

        // when
        val result = option.build()

        // then
        result.shouldNotBeNull()
        result.shouldBeJson(
            """
            {
              "unused5": {
                "${"$"}meta": "searchScore",
                "order": 1
              },
              "unused0": 1,
              "unused1": -1,
              "unused2": 1,
              "unused3": -1,
              "unused4": 1
            }
            """.trimIndent(),
        )
    }

    "should build an ascending order" {
        // given
        data class Collection(
            val string: String?,
            val number: Number?,
            val temporal: Temporal?,
            val stringList: List<String?>?,
            val numberList: List<Number?>?,
            val temporalList: List<Temporal?>?,
        )
        val option = sort {
            "field" by asc
            score by asc
            Collection::string by asc
            Collection::number by asc
            Collection::temporal by asc
            Collection::stringList by asc
            Collection::numberList by asc
            Collection::temporalList by asc
        }

        // when
        val result = option.build()

        // then
        result.shouldNotBeNull()
        result.shouldBeJson(
            """
                {
                  "field": 1,
                  "unused0": {
                    "${"$"}meta": "searchScore",
                    "order": 1
                  },
                  "string": 1,
                  "number": 1,
                  "temporal": 1,
                  "stringList": 1,
                  "numberList": 1,
                  "temporalList": 1
                }
            """.trimIndent(),
        )
    }

    "should build a descending order" {
        // given
        data class Collection(
            val string: String?,
            val number: Number?,
            val temporal: Temporal?,
            val stringList: List<String?>?,
            val numberList: List<Number?>?,
            val temporalList: List<Temporal?>?,
        )
        val option = sort {
            "field" by desc
            score by desc
            Collection::string by desc
            Collection::number by desc
            Collection::temporal by desc
            Collection::stringList by desc
            Collection::numberList by desc
            Collection::temporalList by desc
        }

        // when
        val result = option.build()

        // then
        result.shouldNotBeNull()
        result.shouldBeJson(
            """
                {
                  "field": -1,
                  "unused0": {
                    "${"$"}meta": "searchScore"
                  },
                  "string": -1,
                  "number": -1,
                  "temporal": -1,
                  "stringList": -1,
                  "numberList": -1,
                  "temporalList": -1
                }
            """.trimIndent(),
        )
    }
})
