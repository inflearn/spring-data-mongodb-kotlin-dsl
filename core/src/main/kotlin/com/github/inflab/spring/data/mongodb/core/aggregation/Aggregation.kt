package com.github.inflab.spring.data.mongodb.core.aggregation

import org.springframework.data.mongodb.core.aggregation.Aggregation

/**
 * Configures [Aggregation] using [Aggregation Kotlin DSL][AggregationDsl].
 *
 * @author Jake Son
 * @since 1.0
 * @param configuration The configuration block for the [AggregationDsl].
 */
inline fun aggregation(configuration: AggregationDsl.() -> Unit): Aggregation =
    AggregationDsl().apply(configuration).build()
