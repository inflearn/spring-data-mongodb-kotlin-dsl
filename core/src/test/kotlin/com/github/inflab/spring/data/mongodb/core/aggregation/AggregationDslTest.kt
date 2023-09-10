package com.github.inflab.spring.data.mongodb.core.aggregation

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
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
})
