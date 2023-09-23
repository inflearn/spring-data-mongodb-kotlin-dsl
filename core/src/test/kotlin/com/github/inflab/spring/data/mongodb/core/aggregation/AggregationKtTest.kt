package com.github.inflab.spring.data.mongodb.core.aggregation

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.mongodb.core.aggregation.Aggregation.count
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation

internal class AggregationKtTest : FreeSpec({

    "should create a new Aggregation" {
        // when
        val aggregation = aggregation {
            count("count")
        }

        // then
        aggregation.toString() shouldBe newAggregation(count().`as`("count")).toString()
    }
})
