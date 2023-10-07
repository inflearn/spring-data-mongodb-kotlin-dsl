package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure wildcard path search option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
 */
@AggregationMarker
open class WildcardPathSearchOptionDsl<T> : PathSearchOptionDsl<T>() {
    private val wildcardPath = mutableListOf<String>()

    /**
     * Represents that match any character in the name of the field to search, including nested fields.
     * Wildcard path is only accepted by the following operators:
     *
     * - phrase
     * - regex
     * - text
     * - wildcard
     * - highlighting
     *
     * @param value The wildcard path to search.
     */
    fun wildcard(value: String) {
        wildcardPath.add(value)
    }

    /**
     * Appends a wildcard character to current path.
     */
    fun String.ofWildcard() {
        wildcard("$this*")
    }

    /**
     * Appends a wildcard character to current path.
     */
    fun KProperty<T?>.ofWildcard() {
        wildcard("${this.toDotPath()}.*")
    }

    /**
     * Appends a wildcard character to current path.
     */
    @JvmName("ofWildcardIterable")
    fun KProperty<Iterable<T?>?>.ofWildcard() {
        wildcard("${this.toDotPath()}.*")
    }

    override fun build() = (super.get() + wildcardPath.map { Document("wildcard", it) }).firstOrAll()
}
