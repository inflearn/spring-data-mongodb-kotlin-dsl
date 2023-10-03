package com.github.inflab.spring.data.mongodb.core.extension

import com.github.inflab.spring.data.mongodb.core.aggregation.AggregationDsl
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults

/**
 * Extension for [MongoTemplate.aggregate] providing a [AggregationDsl] as receiver.
 *
 * @author Jake Son
 * @since 1.0
 * @param T The type of the element in the aggregation result.
 * @param O The type of the input data for the aggregation.
 * @param collectionName The name of the input collection to use for the aggregation.
 * @param aggregation The [AggregationDsl] providing the aggregation pipeline.
 * @return The [AggregationResults] of the aggregation.
 * @see [AggregationDsl]
 */
inline fun <reified T, reified O> MongoTemplate.aggregate(
    collectionName: String? = null,
    aggregation: AggregationDsl.() -> Unit,
): AggregationResults<O> =
    aggregate(
        AggregationDsl().apply(aggregation).build(),
        collectionName ?: getCollectionName(T::class.java),
        O::class.java,
    )
