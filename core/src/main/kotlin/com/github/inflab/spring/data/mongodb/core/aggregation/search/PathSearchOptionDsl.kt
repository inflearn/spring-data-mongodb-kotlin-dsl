package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import org.springframework.data.mapping.div
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1

@AggregationMarker
class PathSearchOptionDsl<T> {
    private val path = mutableListOf<String>()

    operator fun String.unaryPlus() {
        path.add(this)
    }

    operator fun KProperty<T>.unaryPlus() {
        path.add(this.toDotPath())
    }

    @JvmName("unaryPlusIterable")
    operator fun KProperty<Iterable<T>>.unaryPlus() {
        path.add(this.toDotPath())
    }

    operator fun <T, U> KProperty<T?>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
        this / other

    @JvmName("timesIterable")
    operator fun <T, U> KProperty<Iterable<T?>>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
        (this as KProperty<T>) / other

    internal fun build() = path.firstOrAll()
}
