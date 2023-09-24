package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchMetaStageDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchStageDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation

/**
 * A Kotlin DSL to configure [Aggregation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class AggregationDsl {
    private val operations = mutableListOf<AggregationOperation>()

    /**
     * Passes a document to the next stage that contains a count of the number of documents input to the stage.
     *
     * @param fieldName The name of the output field which has the count as its value.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/count/#pipe._S_count">$count (aggregation)</a>
     */
    fun count(fieldName: String) {
        operations += Aggregation.count().`as`(fieldName)
    }

    /**
     * Configures a stage that performs a full-text search on the specified field or fields which must be covered by an Atlas Search index.
     *
     * @param searchConfiguration custom configurations for the search stage.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/search">$search (aggregation)</a>
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/">Return Atlas Search Results or Metadata</a>
     */
    fun search(searchConfiguration: SearchStageDsl.() -> Unit) {
        operations += SearchStageDsl().apply(searchConfiguration).build()
    }

    /**
     * Configures a stage that returns different types of metadata result documents.
     *
     * @param searchMetaConfiguration custom configurations for the searchMeta stage.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#-searchmeta">$searchMeta</a>
     */
    fun searchMeta(searchMetaConfiguration: SearchMetaStageDsl.() -> Unit) {
        operations += SearchMetaStageDsl().apply(searchMetaConfiguration).build()
    }

    /**
     * Passes along the documents with the requested fields to the next stage in the pipeline.
     * The specified fields can be existing fields from the input documents or newly computed fields.
     *
     * @param projectConfiguration custom configurations for the project stage.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/project">$project (aggregation)</a>
     */
    fun project(projectConfiguration: ProjectStageDsl.() -> Unit) {
        operations += ProjectStageDsl().apply(projectConfiguration).get()
    }

    /**
     * Builds the [Aggregation] using the configured [AggregationOperation]s.
     *
     * @return The [Aggregation] built using the configured operations.
     */
    fun build(): Aggregation =
        Aggregation.newAggregation(operations)
}
