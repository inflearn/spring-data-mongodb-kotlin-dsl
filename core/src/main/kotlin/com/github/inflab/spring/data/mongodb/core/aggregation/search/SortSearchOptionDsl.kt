package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure sort search option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/sort">Sort Atlas Search Results</a>
 */
@AggregationMarker
class SortSearchOptionDsl {
    private val options = mutableListOf<SortOption>()

    internal sealed interface SortOption {
        data class Field(val field: String, val order: Order) : SortOption
        data class Score(val order: Order) : SortOption
    }

    /**
     * Represents the sort order.
     *
     * @property value The sort order.
     */
    enum class Order(val value: Int) {
        ASC(1),
        DESC(-1),
    }

    /**
     * Sort by the score.
     */
    object Score

    /**
     * Sort by the score.
     */
    val score: Score
        get() = Score

    /**
     * Sort in ascending order.
     * When you sort in ascending order, Atlas Search returns documents with missing values before documents with values.
     */
    val asc: Order
        get() = Order.ASC

    /**
     * Sort in descending order.
     */
    val desc: Order
        get() = Order.DESC

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    infix fun String.by(order: Order) {
        options += SortOption.Field(field = this, order = order)
    }

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    infix fun Score.by(order: Order) {
        options += SortOption.Score(order = order)
    }

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byTemporal")
    infix fun KProperty<Temporal?>.by(order: Order) = this.toDotPath() by order

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byTemporalIterable")
    infix fun KProperty<Iterable<Temporal?>?>.by(order: Order) = this.toDotPath() by order

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byNumber")
    infix fun KProperty<Number?>.by(order: Order) = this.toDotPath() by order

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byNumberIterable")
    infix fun KProperty<Iterable<Number?>?>.by(order: Order) = this.toDotPath() by order

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byString")
    infix fun KProperty<String?>.by(order: Order) = this.toDotPath() by order

    /**
     * Specifies the sort order for the results.
     *
     * @param order The sort order.
     */
    @JvmName("byStringIterable")
    infix fun KProperty<Iterable<String?>?>.by(order: Order) = this.toDotPath() by order

    internal fun build(): Document? {
        if (options.isEmpty()) {
            return null
        }

        val document = Document()
        options.forEach {
            when (it) {
                is SortOption.Field -> document[it.field] = it.order.value
                is SortOption.Score -> document.addScore(it.order)
            }
        }

        return document
    }

    private fun Document.addScore(order: Order) {
        val allFiledNames = options.filterIsInstance<SortOption.Field>().map { it.field }
        val notDuplicatedName = (0..allFiledNames.size)
            .asSequence()
            .map { "unused$it" }
            .first { !allFiledNames.contains(it) }

        this[notDuplicatedName] = Document("\$meta", "searchScore").apply {
            if (order == Order.ASC) put("order", order.value)
        }
    }
}
