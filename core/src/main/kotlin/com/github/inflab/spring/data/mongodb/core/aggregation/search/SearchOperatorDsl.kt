package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure the collection of `$search operators` using idiomatic Kotlin code.
 *
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#operators">$search operators</a>
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class SearchOperatorDsl : SearchOperator {
    override val operations = mutableListOf<Document>()
}
