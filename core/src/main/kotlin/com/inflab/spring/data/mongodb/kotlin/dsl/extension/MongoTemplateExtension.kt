package com.inflab.spring.data.mongodb.kotlin.dsl.extension

import com.inflab.spring.data.mongodb.kotlin.dsl.aggregation.AggregationDsl
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationResults

/**
 * Extension for [MongoTemplate.aggregate] providing a [AggregationDsl] as receiver.
 *
 * @author Jake Son
 * @since 1.0
 * @param T The type of the element in the aggregation result.
 * @param aggregation The [AggregationDsl] providing the aggregation pipeline.
 * @param collectionName The name of the input collection to use for the aggregation.
 * @return The [AggregationResults] of the aggregation.
 * @see [AggregationDsl]
 */
inline fun <reified T> MongoTemplate.aggregate(
    aggregation: AggregationDsl.() -> Unit,
    collectionName: String? = null,
): AggregationResults<T> =
    aggregate(
        AggregationDsl().apply(aggregation).build(),
        collectionName ?: getCollectionName(T::class.java),
        T::class.java,
    )
