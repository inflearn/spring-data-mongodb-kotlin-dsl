package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.VariableOperators.Let
import org.springframework.data.mongodb.core.mapping.Document
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $lookup stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/lookup">$lookup (aggregation)</a>
 */
@AggregationMarker
class LookupStageDsl {
    private val operation = ExtendedLookupOperation()

    /**
     * Specifies the collection in the same database to perform the join with.
     * `from` is optional, you can use a `$documents` stage in a `$lookup` stage instead.
     * For an example, see Use a `$documents` Stage in a `$lookup` Stage.
     *
     * @param from The collection name.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/documents/#std-label-documents-lookup-example">Use a `$documents` Stage in a `$lookup` Stage</a>
     */
    fun from(from: String) {
        operation.from(from)
    }

    /**
     * Specifies the collection in the same database to perform the join with.
     * `from` is optional, you can use a `$documents` stage in a `$lookup` stage instead.
     * For an example, see Use a `$documents` Stage in a `$lookup` Stage.
     *
     * @param from The collection class.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/documents/#std-label-documents-lookup-example">Use a `$documents` Stage in a `$lookup` Stage</a>
     */
    fun from(from: KClass<*>) {
        val annotation = from.annotations.find { it is Document } as? Document?
        if (annotation != null) {
            operation.from(annotation.collection.ifEmpty { annotation.value }.ifEmpty { from.simpleName })
        }

        operation.from(from.simpleName)
    }

    /**
     * Specifies the field from the documents input to the `$lookup` stage.
     * `$lookup` performs an equality match on the localField to the foreignField from the documents of the from collection.
     * If an input document does not contain the localField, the `$lookup` treats the field as having a value of null for matching purposes.
     *
     * @param localField The field from the documents input to the `$lookup` stage.
     */
    fun localField(localField: String) {
        operation.localField(localField)
    }

    /**
     * Specifies the field from the documents input to the `$lookup` stage.
     * `$lookup` performs an equality match on the localField to the foreignField from the documents of the [from] collection.
     * If an input document does not contain the localField, the `$lookup` treats the field as having a value of `null` for matching purposes.
     *
     * @param localField The field from the documents input to the `$lookup` stage.
     */
    fun localField(localField: KProperty<*>) {
        operation.localField(localField.toDotPath())
    }

    /**
     * Specifies the field from the documents in the [from] collection.
     * `$lookup` performs an equality match on the [foreignField] to the [localField] from the input documents.
     * If a document in the [from] collection does not contain the [foreignField], the `$lookup` treats the value as `null` for matching purposes.
     *
     * @param foreignField The field from the documents in the [from] collection.
     */
    fun foreignField(foreignField: String) {
        operation.foreignField(foreignField)
    }

    /**
     * Specifies the field from the documents in the [from] collection.
     * `$lookup` performs an equality match on the [foreignField] to the [localField] from the input documents.
     * If a document in the [from] collection does not contain the [foreignField], the `$lookup` treats the value as `null` for matching purposes.
     *
     * @param foreignField The field from the documents in the [from] collection.
     */
    fun foreignField(foreignField: KProperty<*>) {
        operation.foreignField(foreignField.toDotPath())
    }

    /**
     * Specifies the pipeline to run on the joined collection.
     * The `pipeline` determines the resulting documents from the joined collection.
     * To return all documents, specify an empty `pipeline` `[]`.
     *
     * @param configuration The configuration block for [AggregationDsl].
     */
    fun pipeline(configuration: AggregationDsl.() -> Unit) {
        operation.pipeline(AggregationDsl().apply(configuration).build().pipeline)
    }

    /**
     * Specifies variables to use in the `pipeline` stages.
     * Use the variable expressions to access the fields from the joined collection's documents that are input to the `pipeline`.
     *
     * @param let The variables to use in the `pipeline` stages.
     */
    fun let(let: Let) {
        operation.let(let)
    }

    /**
     * Specifies the name of the new array field to add to the joined documents.
     * The new array field contains the matching documents from the joined collection.
     * If the specified name already exists in the joined document, the existing field is overwritten.
     *
     * @param `as` The name of the new array field to add to the joined documents.
     */
    fun `as`(`as`: String) {
        operation.`as`(`as`)
    }

    internal fun get() = operation
}
