package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the facet collector using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet">facet</a>
 */
@AggregationMarker
class FacetSearchCollectorDsl {
    private val document = Document()
    private val facets = Document()

    /**
     * Configures the [SearchOperator]s to use to perform the facet over.
     * If omitted, Atlas Search performs the facet over all documents in the collection.
     *
     * @param operatorConfiguration The configuration block for the [SearchOperatorDsl].
     */
    fun operator(operatorConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(operatorConfiguration).operators

        document["operator"] = Document().apply { operators.forEach { putAll(it) } }
    }

    /**
     * Configures the string facet that allow you to narrow down Atlas Search results based on the most frequent string values in the specified string field.
     * Note that the string field must be indexed as [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     *
     * @param path Field path to facet on. You can specify a field that is indexed as a [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     * @param numBuckets Maximum number of facet categories to return in the results.
     * Value must be less than or equal to 1000.
     * If specified, Atlas Search may return fewer categories than requested if the data is grouped into fewer categories than your requested number.
     * If omitted, defaults to 10, which means that Atlas Search will return only the top 10 facet categories by count.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#string-facets">String Facets</a>
     */
    fun String.stringFacet(path: String, numBuckets: Int? = null) {
        facets[this] = Document().apply {
            put("type", "string")
            put("path", path)
            numBuckets?.let { put("numBuckets", it) }
        }
    }

    /**
     * Configures the string facet that allow you to narrow down Atlas Search results based on the most frequent string values in the specified string field.
     * Note that the string field must be indexed as [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     *
     * @param path Field path to facet on. You can specify a field that is indexed as a [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     * @param numBuckets Maximum number of facet categories to return in the results.
     * Value must be less than or equal to 1000.
     * If specified, Atlas Search may return fewer categories than requested if the data is grouped into fewer categories than your requested number.
     * If omitted, defaults to 10, which means that Atlas Search will return only the top 10 facet categories by count.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#string-facets">String Facets</a>
     */
    fun String.stringFacet(path: KProperty<String>, numBuckets: Int? = null) {
        stringFacet(path.toDotPath(), numBuckets)
    }

    /**
     * Configures the numeric facet that allow you to determine the frequency of numeric values in your search results by breaking the results into separate ranges of numbers.
     *
     * @param path Field path to facet on. You can specify a field that is indexed as a [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     * @param boundaries List of numeric values, in ascending order, that specify the boundaries for each bucket.
     * You must specify at least two boundaries.
     * Each adjacent pair of values acts as the inclusive lower bound and the exclusive upper bound for the bucket.
     * @param default Name of an additional bucket that counts documents returned from the operator that do not fall within the specified boundaries.
     * If omitted, Atlas Search includes the results of the facet operator that do not fall under a specified bucket also, but doesn't include it in any bucket counts.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#numeric-facets">Numeric Facets</a>
     */
    fun String.numberFacet(path: String, boundaries: List<Number>, default: String? = null) {
        facets[this] = Document().apply {
            put("type", "number")
            put("path", path)
            put("boundaries", boundaries)
            default?.let { put("default", it) }
        }
    }

    /**
     * Configures the numeric facet that allow you to determine the frequency of numeric values in your search results by breaking the results into separate ranges of numbers.
     *
     * @param path Field path to facet on. You can specify a field that is indexed as a [How to Index String Fields For Faceted Search](https://www.mongodb.com/docs/atlas/atlas-search/field-types/string-facet-type/#std-label-bson-data-types-string-facet).
     * @param boundaries List of numeric values, in ascending order, that specify the boundaries for each bucket.
     * You must specify at least two boundaries.
     * Each adjacent pair of values acts as the inclusive lower bound and the exclusive upper bound for the bucket.
     * @param default Name of an additional bucket that counts documents returned from the operator that do not fall within the specified boundaries.
     * If omitted, Atlas Search includes the results of the facet operator that do not fall under a specified bucket also, but doesn't include it in any bucket counts.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#numeric-facets">Numeric Facets</a>
     */
    fun String.numberFacet(path: KProperty<Number>, boundaries: List<Number>, default: String? = null) {
        numberFacet(path.toDotPath(), boundaries, default)
    }

    /**
     * Configures the date facet that allow you to narrow down search results based on a date.
     *
     * @param definition The configuration block for the [DateFacetDefinitionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#date-facets">Date Facets</a>
     */
    infix fun String.dateFacet(definition: DateFacetDefinitionDsl.() -> Unit) {
        facets[this] = DateFacetDefinitionDsl().apply(definition).get()
    }

    internal fun build(): Document {
        document["facets"] = facets

        return Document("facet", document)
    }
}
