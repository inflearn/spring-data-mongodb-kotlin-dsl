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
        Ascending,
        Descending,
        TextScore,
    }

    /**
     * Sort ascending.
     */
    val Ascending: Order = Order.Ascending

    /**
     * Sort descending.
     */
    val Descending: Order = Order.Descending

    /**
     * Sort by the computed `textScore` metadata in descending order.
     *
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/sort/#std-label-sort-pipeline-metadata">Text Score Metadata Sort</a>
     */
    val TextScore: Order = Order.TextScore

    /**
     * Specifies the field to sort by.
     *
     * @param order The order to sort by.
     */
    infix fun String.by(order: Order): Boolean {
        when (order) {
            Order.Ascending -> operation.ascending(this)
            Order.Descending -> operation.descending(this)
            Order.TextScore -> operation.textScore(this)
        }
        return true
    }

    /**
     * Specifies the field to sort by.
     *
     * @param order The order to sort by.
     */
    infix fun KProperty<*>.by(order: Order): Boolean =
        this.toDotPath() by order

    internal fun get() = operation
}
