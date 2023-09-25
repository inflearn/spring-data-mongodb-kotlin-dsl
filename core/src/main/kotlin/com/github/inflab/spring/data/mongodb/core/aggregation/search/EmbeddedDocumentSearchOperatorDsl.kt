package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure embedded document search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/embedded-document">embeddedDocument</a>
 */
@AggregationMarker
class EmbeddedDocumentSearchOperatorDsl {
    private val document = Document()

    /**
     * Configures operator to use to query each document in the array of documents that you specify in the path.
     * The `moreLikeThis` operator is not supported.
     *
     * @param operatorConfiguration The configuration block for the [SearchOperatorDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/operators-and-collectors/#operators">Operators</a>
     */
    fun operator(operatorConfiguration: SearchOperatorDsl.() -> Unit) {
        val operators = SearchOperatorDsl().apply(operatorConfiguration).operators

        document["operator"] = Document().apply { operators.forEach { putAll(it) } }
    }

    /**
     * Specifies the field to search.
     * It must be a parent for all operators and fields specified using the operator option.
     *
     * @param path The indexed field in arrays of objects and documents.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/embedded-documents-type/#std-label-bson-data-types-embedded-documents">How to Index Fields in Arrays of Objects and Documents</a>
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Specifies the field to search.
     * It must be a parent for all operators and fields specified using the operator option.
     *
     * @param path The indexed field in arrays of objects and documents.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/embedded-documents-type/#std-label-bson-data-types-embedded-documents">How to Index Fields in Arrays of Objects and Documents</a>
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun <T> path(path: KProperty<Iterable<T>>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Score to assign to matching search results.
     * You can use the [embedded][ScoreSearchOptionDsl.embedded] scoring option to configure scoring options.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/embedded-document/#std-label-embedded-document-query-score">Scoring Behavior</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    internal fun build() = Document("embeddedDocument", document)
}
