package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline
import org.springframework.data.mongodb.core.aggregation.UnionWithOperation
import org.springframework.data.mongodb.core.mapping.Document
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

/**
 * A Kotlin DSL to configure $unionWith stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/unionWith">$unionWith (aggregation)</a>
 */
@AggregationMarker
class UnionWithStageDsl {
    private var collection: String? = null
    private var pipeline: AggregationPipeline? = null

    /**
     * The collection or view whose `pipeline` results you wish to include in the result set.
     *
     * @param collection The collection name.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unionWith/#std-label-unionWith-coll">coll</a>
     */
    fun coll(collection: String) {
        this.collection = collection
    }

    /**
     * The collection or view whose `pipeline` results you wish to include in the result set.
     *
     * @param collection The collection class.
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unionWith/#std-label-unionWith-coll">coll</a>
     */
    fun coll(collection: KClass<*>) {
        val annotation = collection.findAnnotation<Document>()
        this.collection = annotation?.collection?.ifEmpty { annotation.value.takeIf { it.isNotEmpty() } }
            ?: collection.simpleName
    }

    /**
     * An aggregation pipeline to apply to the specified [coll].
     *
     * `[ <stage1>, <stage2>, ...]`
     *
     * The pipeline cannot include the `$out` and `$merge` stages.
     * Starting in v6.0, the `pipeline` can contain the `Atlas Search $search` stage as the first stage inside the pipeline.
     *
     * @param configuration The configuration for [AggregationDsl].
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/unionWith/#std-label-unionWith-pipeline">pipeline</a>
     */
    fun pipeline(configuration: AggregationDsl.() -> Unit) {
        pipeline = AggregationDsl().apply(configuration).build().pipeline
    }

    internal fun build(): UnionWithOperation? {
        val operation = collection?.let { UnionWithOperation.unionWith(it) } ?: return null

        return pipeline?.let { operation.pipeline(it) } ?: operation
    }
}
