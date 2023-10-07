package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $sort stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sort">$sort (aggregation)</a>
 */
@AggregationMarker
class SortStageDsl {
    private val operation = ExtendedSortOperation()

    /**
     * Represents the sort order.
     */
    enum class Order {
        ASC,
        DESC,
        TextScore,
    }

    /**
     * Sort ascending.
     */
    val asc: Order
        get() = Order.ASC

    /**
     * Sort descending.
     */
    val desc: Order
        get() = Order.DESC

    /**
     * Sort by the computed `textScore` metadata in descending order.
     *
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sort/#std-label-sort-pipeline-metadata">Text Score Metadata Sort</a>
     */
    val textScore: Order
        get() = Order.TextScore

    /**
     * Specifies the field to sort by.
     *
     * @param order The order to sort by.
     */
    infix fun String.by(order: Order) {
        when (order) {
            Order.ASC -> operation.ascending(this)
            Order.DESC -> operation.descending(this)
            Order.TextScore -> operation.textScore(this)
        }
    }

    /**
     * Specifies the field to sort by.
     *
     * @param order The order to sort by.
     */
    infix fun KProperty<*>.by(order: Order) =
        this.toDotPath() by order

    internal fun get() = operation
}
