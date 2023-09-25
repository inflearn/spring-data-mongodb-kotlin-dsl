package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the data facet definition using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#date-facets">Data Facets</a>
 */
@AggregationMarker
class DateFacetDefinitionDsl {
    private val document = Document("type", "date")

    /**
     * List of date values that specify the boundaries for each bucket. You must specify:
     *
     * - At least two boundaries
     * - Values in ascending order, with the earliest date first
     *
     * Each adjacent pair of values acts as the inclusive lower bound and the exclusive upper bound for the bucket.
     *
     * @param boundaries List of date values that specify the boundaries for each bucket.
     */
    fun boundaries(vararg boundaries: Temporal) {
        document["boundaries"] = boundaries.toList()
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index Date Fields.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/date-type/#std-label-bson-data-types-date">How to Index Date Fields</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Field path to facet on. You can specify a field that is indexed as a How to Index Date Fields.
     *
     * @param path Field path to facet on.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/date-type/#std-label-bson-data-types-date">How to Index Date Fields</a>
     */
    fun path(path: KProperty<Temporal>) {
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
