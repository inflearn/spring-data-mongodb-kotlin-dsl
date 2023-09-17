package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure the collection of `$search collectors` using idiomatic Kotlin code.
 *
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#collectors">$search collectors</a>
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class SearchCollectorDsl : SearchCollector {
    override val collectors = mutableListOf<Document>()

    override fun facet(facetConfiguration: FacetCollectorDsl.() -> Unit) {
        collectors.add(FacetCollectorDsl().apply(facetConfiguration).build())
    }
}
