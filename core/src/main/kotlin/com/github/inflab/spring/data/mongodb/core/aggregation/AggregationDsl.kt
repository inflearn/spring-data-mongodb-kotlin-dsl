package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation

/**
 * A Kotlin DSL to configure [Aggregation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class AggregationDsl {
    private val operations = mutableListOf<AggregationOperation>()

    /**
     * Passes a document to the next stage that contains a count of the number of documents input to the stage.
     *
     * @param fieldName The name of the output field which has the count as its value.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/count/#pipe._S_count">https://docs.mongodb.com/manual/reference/operator/aggregation/count/</a>
     */
    fun count(fieldName: String) {
        operations += Aggregation.count().`as`(fieldName)
    }

    fun build(): Aggregation =
        Aggregation.newAggregation(operations)
}
