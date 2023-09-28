package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import kotlin.reflect.KProperty

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
    operator fun KProperty<T?>.unaryPlus() {
        path.add(this.toDotPath())
    }

    /**
     * The indexed array field to search.
     */
    @JvmName("unaryPlusIterable")
    operator fun KProperty<Iterable<T?>?>.unaryPlus() {
        path.add(this.toDotPath())
    }

    internal fun build() = path.firstOrAll()
}
