package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.search.PathSearchOptionDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchMetaStageDsl
import com.github.inflab.spring.data.mongodb.core.aggregation.search.SearchStageDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.AggregationOptions
import org.springframework.data.mongodb.core.aggregation.AggregationOptions.DomainTypeMapping
import org.springframework.data.mongodb.core.aggregation.UnsetOperation
import org.springframework.data.mongodb.core.query.Collation
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.CriteriaDefinition
import kotlin.reflect.KProperty
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
     * Adds a raw [stage][AggregationOperation] to the pipeline.
     *
     * @param stage The [AggregationOperation] to add to the pipeline.
     */
    fun stage(stage: AggregationOperation) {
        operations += stage
    }

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
     * @param searchConfiguration The configuration block for the [SearchStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/search">$search (aggregation)</a>
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/">Return Atlas Search Results or Metadata</a>
     */
    fun search(searchConfiguration: SearchStageDsl.() -> Unit) {
        operations += SearchStageDsl().apply(searchConfiguration).build()
    }

    /**
     * Configures a stage that returns different types of metadata result documents.
     *
     * @param searchMetaConfiguration The configuration block for the [SearchMetaStageDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/query-syntax/#-searchmeta">$searchMeta</a>
     */
    fun searchMeta(searchMetaConfiguration: SearchMetaStageDsl.() -> Unit) {
        operations += SearchMetaStageDsl().apply(searchMetaConfiguration).build()
    }

    /**
     * Passes along the documents with the requested fields to the next stage in the pipeline.
     * The specified fields can be existing fields from the input documents or newly computed fields.
     *
     * @param projectConfiguration The configuration block for the [ProjectStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/project">$project (aggregation)</a>
     */
    fun project(projectConfiguration: ProjectStageDsl.() -> Unit) {
        operations += ProjectStageDsl().apply(projectConfiguration).get()
    }

    /**
     * Configures a stage that sorts all input documents and returns them to the pipeline in sorted order.
     *
     * @param sortConfiguration The configuration block for the [SortStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sort">$sort (aggregation)</a>
     */
    fun sort(sortConfiguration: SortStageDsl.() -> Unit) {
        operations += SortStageDsl().apply(sortConfiguration).get()
    }

    /**
     * Configures a stage that groups incoming documents based on the value of a specified expression, then computes the count of documents in each distinct group.
     * Each output document contains two fields: an `_id` field containing the distinct grouping value, and a `count` field containing the number of documents belonging to that grouping or category.
     * The documents are sorted by `count` in descending order.
     *
     * @param field The field path.
     * @see <a href="https://www.mongodb.com/docs/v7.0/meta/aggregation-quick-reference/#std-label-agg-quick-ref-field-paths">Field Paths</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sortByCount">$sortByCount (aggregation)</a>
     */
    fun sortByCount(field: String) {
        operations += Aggregation.sortByCount(field)
    }

    /**
     * Configures a stage that groups incoming documents based on the value of a specified expression, then computes the count of documents in each distinct group.
     * Each output document contains two fields: an `_id` field containing the distinct grouping value, and a `count` field containing the number of documents belonging to that grouping or category.
     * The documents are sorted by `count` in descending order.
     *
     * @param field The field path.
     * @see <a href="https://www.mongodb.com/docs/v7.0/meta/aggregation-quick-reference/#std-label-agg-quick-ref-field-paths">Field Paths</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sortByCount">$sortByCount (aggregation)</a>
     */
    fun sortByCount(field: KProperty<*>) {
        sortByCount(field.toDotPath())
    }

    /**
     * Configures a stage that groups incoming documents based on the value of a specified expression, then computes the count of documents in each distinct group.
     * Each output document contains two fields: an `_id` field containing the distinct grouping value, and a `count` field containing the number of documents belonging to that grouping or category.
     * The documents are sorted by `count` in descending order.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/meta/aggregation-quick-reference/#expressions">Expressions</a>
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/sortByCount">$sortByCount (aggregation)</a>
     */
    fun sortByCount(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        operations += Aggregation.sortByCount(AggregationExpressionDsl().configuration())
    }

    /**
     * Creates a new $match stage using the given [Criteria].
     *
     * @param criteria The [Criteria] to match documents against.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/match/">$match (aggregation)</a>
     */
    fun match(criteria: Criteria) {
        operations += Aggregation.match(criteria)
    }

    /**
     * Creates a new $match stage using the given [CriteriaDefinition].
     *
     * @param criteria The [CriteriaDefinition] to match documents against.
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/match/">$match (aggregation)</a>
     */
    fun match(criteria: CriteriaDefinition) {
        operations += Aggregation.match(criteria)
    }

    /**
     * Creates a new $match stage using the given [AggregationExpression].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/match/">$match (aggregation)</a>
     */
    fun match(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        operations += Aggregation.match(AggregationExpressionDsl().configuration())
    }

    /**
     * Configures a stage that deconstructs an array field from the input documents to output a document for each element.
     * Each output document is the input document with the value of the array field replaced by the element.
     *
     * @param unwindConfiguration The configuration block for the [UnwindStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/unwind">$unwind (aggregation)</a>
     */
    fun unwind(unwindConfiguration: UnwindStageDsl.() -> Unit) {
        UnwindStageDsl().apply(unwindConfiguration).build()?.let { operations += it }
    }

    /**
     * Configures a stage that performs a left outer join to a collection in the same database to filter in documents from the "joined" collection for processing.
     * The `$lookup` stage adds a new array field to each input document.
     * The new array field contains the matching documents from the "joined" collection.
     * The `$lookup` stage passes these reshaped documents to the next stage.
     *
     * @param lookupConfiguration The configuration block for the [LookupStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/lookup">$lookup (aggregation)</a>
     */
    fun lookup(lookupConfiguration: LookupStageDsl.() -> Unit) {
        operations += LookupStageDsl().apply(lookupConfiguration).get()
    }

    /**
     * Configures a stage that removes/excludes fields from documents.
     *
     * @param pathConfiguration The configuration block for the [PathSearchOptionDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/unset">$unset (aggregation)</a>
     */
    fun unset(pathConfiguration: PathSearchOptionDsl<Any>.() -> Unit) {
        operations += UnsetOperation(PathSearchOptionDsl<Any>().apply(pathConfiguration).get() as Collection<Any>)
    }

    /**
     * Configures a stage that performs a union of two collections.
     * `$unionWith` combines pipeline results from two collections into a single result set.
     * The stage outputs the combined result set (including duplicates) to the next stage.
     *
     * The order in which the combined result set documents are output is unspecified.
     *
     * @param unionWithConfiguration The configuration block for the [UnionWithStageDsl].
     * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/unionWith/">$unionWith (aggregation)</a>
     */
    fun unionWith(unionWithConfiguration: UnionWithStageDsl.() -> Unit) {
        UnionWithStageDsl().apply(unionWithConfiguration).build()?.let { operations += it }
    }

    /**
     * Configures a stage that skips over the specified number of documents that pass into the stage and passes the remaining documents to the next stage in the pipeline.
     *
     * @param elementsToSkip Must not be less than zero.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/skip/">$skip (aggregation)</a>
     */
    fun skip(elementsToSkip: Long) {
        operations += Aggregation.skip(elementsToSkip)
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
