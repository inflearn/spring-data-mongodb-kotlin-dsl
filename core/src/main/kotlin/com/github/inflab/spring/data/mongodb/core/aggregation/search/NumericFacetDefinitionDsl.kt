package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the number facet definition using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#numeric-facets">Numeric Facets</a>
 */
@AggregationMarker
class NumericFacetDefinitionDsl {
    private val document = Document("type", "number")

    /**
     * List of numeric values, in ascending order, that specify the boundaries for each bucket.
     * You must specify at least two boundaries.
     * Each adjacent pair of values acts as the inclusive lower bound and the exclusive upper bound for the bucket.
     *
     * @param boundaries List of numeric values.
     */
    fun boundaries(vararg boundaries: Number) {
        document["boundaries"] = boundaries.toList()
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index Numeric Values.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/number-type/#std-label-bson-data-types-number">How to Index Numeric Values</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index Numeric Values.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/number-type/#std-label-bson-data-types-number">How to Index Numeric Values</a>
     */
    fun path(path: KProperty<Number?>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Name of an additional bucket that counts documents returned from the operator that do not fall within the specified boundaries.
     * If omitted, Atlas Search includes the results of the facet operator that do not fall under a specified bucket also, but Atlas Search doesn't include these results in any bucket counts.
     *
     * @param value Name of an additional bucket that counts documents returned from the operator that do not fall within the specified boundaries.
     */
    fun default(value: String) {
        document["default"] = value
    }

    internal fun get() = document
}
