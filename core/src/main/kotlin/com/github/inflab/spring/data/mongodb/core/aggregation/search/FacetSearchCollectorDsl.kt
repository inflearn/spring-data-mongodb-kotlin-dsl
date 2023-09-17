package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

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
     * Note that the string field must be indexed as How to Index String Fields For Faceted Search.
     *
     * @param configuration The configuration block for the [StringFacetDefinitionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#string-facets">String Facets</a>
     */
    infix fun String.stringFacet(configuration: StringFacetDefinitionDsl.() -> Unit) {
        facets[this] = StringFacetDefinitionDsl().apply(configuration).get()
    }

    /**
     * Configures the numeric facet that allow you to determine the frequency of numeric values in your search results by breaking the results into separate ranges of numbers.
     *
     * @param configuration The configuration block for the [NumericFacetDefinitionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#numeric-facets">Numeric Facets</a>
     */
    infix fun String.numericFacet(configuration: NumericFacetDefinitionDsl.() -> Unit) {
        facets[this] = NumericFacetDefinitionDsl().apply(configuration).get()
    }

    /**
     * Configures the date facet that allow you to narrow down search results based on a date.
     *
     * @param configuration The configuration block for the [DateFacetDefinitionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet/#date-facets">Date Facets</a>
     */
    infix fun String.dateFacet(configuration: DateFacetDefinitionDsl.() -> Unit) {
        facets[this] = DateFacetDefinitionDsl().apply(configuration).get()
    }

    internal fun build(): Document {
        document["facets"] = facets

        return Document("facet", document)
    }
}
