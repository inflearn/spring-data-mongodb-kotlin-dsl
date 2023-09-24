package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import org.springframework.data.mapping.div
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

/**
 * A Kotlin DSL to configure path search option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
 */
@AggregationMarker
class PathSearchOptionDsl<T> {
    private val path = mutableListOf<String>()

    /**
     * The indexed field to search.
     */
    operator fun String.unaryPlus() {
        path.add(this)
    }

    /**
     * The indexed field to search.
     */
    operator fun KProperty<T>.unaryPlus() {
        path.add(this.toDotPath())
    }

    /**
     * The indexed array field to search.
     */
    @JvmName("unaryPlusIterable")
    operator fun KProperty<Iterable<T>>.unaryPlus() {
        path.add(this.toDotPath())
    }

    /**
     * Builds nested path from Property References.
     * For example, referring to the field "author.name":
     * ```
     * path(Book::author..Author::name)
     * ```
     */
    operator fun <T, U> KProperty<T?>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
        this / other

    /**
     * Builds nested path from Property References.
     * For example, referring to the field "author.names":
     * ```
     * path(Book::author..Author::names)
     * ```
     */
    @JvmName("timesIterable")
    operator fun <T, U> KProperty<Iterable<T?>>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
        (this as KProperty<T>) / other

    internal fun build() = path.firstOrAll()
}
