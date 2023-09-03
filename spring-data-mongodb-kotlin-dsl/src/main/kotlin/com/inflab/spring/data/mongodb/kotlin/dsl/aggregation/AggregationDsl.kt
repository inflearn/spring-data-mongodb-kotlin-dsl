package com.inflab.spring.data.mongodb.kotlin.dsl.aggregation

import com.inflab.spring.data.mongodb.kotlin.dsl.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.Aggregation

/**
 * A Kotlin DSL to configure [Aggregation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class AggregationDsl {

    fun build(): Aggregation {
        return Aggregation.newAggregation()
    }
}
