package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure multi path search option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
 */
@AggregationMarker
class MultiWildcardPathSearchOptionDsl<T> : WildcardPathSearchOptionDsl<T>() {
    private val multiPath = mutableListOf<Document>()

    /**
     * The name of the alternate analyzer specified in a multi object in an index definition.
     *
     * @param multi The name of the alternate analyzer.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/analyzers/multi/#std-label-ref-multi-analyzers">Multi Analyzer</a>
     */
    infix fun String.multi(multi: String) {
        multiPath.add(Document("value", this).append("multi", multi))
    }

    /**
     * The name of the alternate analyzer specified in a multi object in an index definition.
     *
     * @param multi The name of the alternate analyzer.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/analyzers/multi/#std-label-ref-multi-analyzers">Multi Analyzer</a>
     */
    infix fun KProperty<T?>.multi(multi: String) {
        multiPath.add(Document("value", this.toDotPath()).append("multi", multi))
    }

    /**
     * The name of the alternate analyzer specified in a multi object in an index definition.
     *
     * @param multi The name of the alternate analyzer.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/analyzers/multi/#std-label-ref-multi-analyzers">Multi Analyzer</a>
     */
    @JvmName("multiIterable")
    infix fun KProperty<Iterable<T?>?>.multi(multi: String) {
        multiPath.add(Document("value", this.toDotPath()).append("multi", multi))
    }

    override fun build(): Any? {
        val wildcardPath = super.build() ?: return multiPath.firstOrAll()

        if (wildcardPath is List<*>) {
            return (wildcardPath + multiPath).firstOrAll()
        }

        return listOf(wildcardPath, multiPath).firstOrAll()
    }
}
