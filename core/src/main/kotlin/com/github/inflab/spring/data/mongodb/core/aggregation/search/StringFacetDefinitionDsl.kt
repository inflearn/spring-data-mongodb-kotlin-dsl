package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the string facet definition using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#string-facets">String Facets</a>
 */
@AggregationMarker
class StringFacetDefinitionDsl {
    private val document = Document("type", "string")

    /**
     * Maximum number of facet categories to return in the results.
     * Value must be less than or equal to 1000.
     * If specified, Atlas Search may return fewer categories than requested if the data is grouped into fewer categories than your requested number.
     * If omitted, defaults to 10, which means that Atlas Search will return only the top 10 facet categories by count.
     *
     * @param value Maximum number of facet categories to return in the results.
     */
    fun numBuckets(value: Int) {
        document["numBuckets"] = value
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index String Fields For Faceted Search.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet">How to Index String Fields For Faceted Search</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index String Fields For Faceted Search.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet">How to Index String Fields For Faceted Search</a>
     */
    fun path(path: KProperty<String>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index String Fields For Faceted Search.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet">How to Index String Fields For Faceted Search</a>
     */
    @JvmName("pathIterable")
    fun path(path: KProperty<Iterable<String>>) {
        document["path"] = path.toDotPath()
    }

    internal fun get() = document
}
