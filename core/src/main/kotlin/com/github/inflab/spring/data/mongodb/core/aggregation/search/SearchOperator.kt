package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document

/**
 *  Interface that specifies a MongoDB `$search` operators.
 *
 *  @author Jake Son
 *  @since 1.0
 *  @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#operators">$search operators</a>
 */
interface SearchOperator {
    val operators: MutableList<Document>
}
