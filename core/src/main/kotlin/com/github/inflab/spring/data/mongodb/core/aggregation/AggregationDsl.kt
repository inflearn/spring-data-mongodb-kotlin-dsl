package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchMetaStageDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchStageDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.aggregation.AggregationOptions.DomainTypeMapping
import org.springframework.data.mongodb.core.query.Collation
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * A Kotlin DSL to configure [Aggregation] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class AggregationDsl {
    private val operations = mutableListOf<AggregationOperation>()
    private var options: AggregationOptions? = null

    /**
     * Configures a set of aggregation options that can be used within an aggregation pipeline.
     *
     * @param allowDiskUse Override `allowDiskUseByDefault` for a specific query.
     * @param explain Specifies to return the information on the processing of the pipeline.
     * @param cursor Specify a document that contains options that control the creation of the cursor object.
     * @param cursorBatchSize Indicates a cursor with a non-default batch size.
     * @param collation Specifies the collation to use for the operation.
     * @param comment A user-provided comment to attach to this command.
     * @param hint The index to use for the aggregation.
     * @param maxTime Specifies a time limit in [Duration].
     * @param skipOutput Skip results when running an aggregation.
     * Useful in combination with `$merge` or `$out`.
     * @param domainTypeMapping Specifies a domain type mappings supported by the mapping layer.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/command/aggregate/#command-fields">Aggregation Options</a>
     */
    fun options(
        allowDiskUse: Boolean? = null,
        explain: Boolean? = null,
        cursor: Document? = null,
        cursorBatchSize: Int? = null,
        collation: Collation? = null,
        comment: String? = null,
        hint: Document? = null,
        maxTime: Duration? = null,
        skipOutput: Boolean? = null,
        domainTypeMapping: DomainTypeMapping? = null,
    ) {
        val builder = AggregationOptions.builder()
        allowDiskUse?.let { builder.allowDiskUse(it) }
        explain?.let { builder.explain(it) }
        cursor?.let { builder.cursor(it) }
        cursorBatchSize?.let { builder.cursorBatchSize(it) }
        collation?.let { builder.collation(it) }
        comment?.let { builder.comment(it) }
        hint?.let { builder.hint(it) }
        maxTime?.let { builder.maxTime(it.toJavaDuration()) }
        if (skipOutput == true) builder.skipOutput()
        when (domainTypeMapping) {
            DomainTypeMapping.STRICT -> builder.strictMapping()
            DomainTypeMapping.RELAXED -> builder.relaxedMapping()
            DomainTypeMapping.NONE -> builder.noMapping()
            null -> {}
        }

        options = builder.build()
    }

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
     * Configures a stage that sorts all input documents and returns them to the pipeline in sorted order.
     *
     * @param sortConfiguration custom configurations for the sort stage.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sort">$sort (aggregation)</a>
     */
    fun sort(sortConfiguration: SortStageDsl.() -> Unit) {
        operations += SortStageDsl().apply(sortConfiguration).get()
    }

    /**
     * Builds the [Aggregation] using the configured [AggregationOperation]s.
     *
     * @return The [Aggregation] built using the configured operations.
     */
    fun build(): Aggregation {
        val aggregation = Aggregation.newAggregation(operations)

        return options?.let { aggregation.withOptions(it) } ?: aggregation
    }
}
