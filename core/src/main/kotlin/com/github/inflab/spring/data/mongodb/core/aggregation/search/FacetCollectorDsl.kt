package com.github.inflab.spring.data.mongodb.core.aggregation.search

import org.bson.Document

class FacetCollectorDsl {
    private val document = Document()

    internal fun build() = Document("facet", document)
}
