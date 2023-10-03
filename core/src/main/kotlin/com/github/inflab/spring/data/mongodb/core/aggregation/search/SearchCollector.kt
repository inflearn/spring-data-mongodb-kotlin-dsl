package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document

/**
 *  Interface that specifies a MongoDB `$search` collectors.
 *
 *  @author Jake Son
 *  @since 1.0
 *  @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#collectors">$search collectors</a>
 */
interface SearchCollector {
    /**
     * A list of `$search` collectors.
     */
    val collectors: MutableList<Document>

    /**
     * Groups query results by values or ranges in specified, faceted fields and returns the count for each of those groups.
     *
     * @param facetConfiguration A [FacetSearchCollectorDsl] to configure the `$facet` stage.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/facet">facet</a>
     */
    fun facet(facetConfiguration: FacetSearchCollectorDsl.() -> Unit)
}
