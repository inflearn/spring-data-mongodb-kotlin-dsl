package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document

/**
 * A Kotlin DSL to configure the collection of `$search operators` using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#operators">$search operators</a>
 */
@AggregationMarker
class SearchOperatorDsl : SearchOperator {
    override val operators = mutableListOf<Document>()

    override fun text(configuration: TextSearchOperatorDsl.() -> Unit) {
        operators.add(TextSearchOperatorDsl().apply(configuration).build())
    }

    override fun equal(configuration: EqualsSearchOperatorDsl.() -> Unit) {
        operators.add(EqualsSearchOperatorDsl().apply(configuration).build())
    }

    override fun compound(configuration: CompoundSearchOperatorDsl.() -> Unit) {
        operators.add(CompoundSearchOperatorDsl().apply(configuration).build())
    }

    override fun phrase(configuration: PhraseSearchOperatorDsl.() -> Unit) {
        operators.add(PhraseSearchOperatorDsl().apply(configuration).build())
    }

    override fun range(configuration: RangeSearchOperatorDsl.() -> Unit) {
        operators.add(RangeSearchOperatorDsl().apply(configuration).build())
    }

    override fun embeddedDocument(configuration: EmbeddedDocumentSearchOperatorDsl.() -> Unit) {
        operators.add(EmbeddedDocumentSearchOperatorDsl().apply(configuration).build())
    }

    override fun exists(configuration: ExistsSearchOperatorDsl.() -> Unit) {
        operators.add(ExistsSearchOperatorDsl().apply(configuration).build())
    }

    override fun geoShape(configuration: GeoShapeSearchOperatorDsl.() -> Unit) {
        operators.add(GeoShapeSearchOperatorDsl().apply(configuration).build())
    }
}
