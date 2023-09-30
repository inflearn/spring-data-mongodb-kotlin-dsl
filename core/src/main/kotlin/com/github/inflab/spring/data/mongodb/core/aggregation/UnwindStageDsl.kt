package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.UnwindOperation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $unwind stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/unwind">$unwind (aggregation)</a>
 */
@AggregationMarker
class UnwindStageDsl {
    private var path: String? = null

    /**
     * The name of a new field to hold the array index of the element.
     * The name cannot start with a dollar sign $.
     */
    var includeArrayIndex: String = ""

    /**
     * Optional.
     *
     * - If true, if the path is null, missing, or an empty array, `$unwind` outputs the document.
     * - If false, if path is null, missing, or an empty array, `$unwind` does not output a document.
     *
     * The default value is `false`.
     */
    var preserveNullAndEmptyArrays: Boolean = false

    /**
     * Field path to an array field.
     *
     * @param path The field path to an array field.
     */
    fun path(path: String) {
        this.path = path
    }

    /**
     * Field path to an array field.
     *
     * @param path The field path to an array field.
     */
    fun path(path: KProperty<*>) {
        this.path = path.toDotPath()
    }

    internal fun build(): UnwindOperation? {
        val builder = path?.let { UnwindOperation.newUnwind().path(it) } ?: return null

        val indexBuilder = when (includeArrayIndex.isBlank()) {
            true -> builder.noArrayIndex()
            false -> builder.arrayIndex(includeArrayIndex)
        }

        return when (preserveNullAndEmptyArrays) {
            true -> indexBuilder.preserveNullAndEmptyArrays()
            false -> indexBuilder.skipNullAndEmptyArrays()
        }
    }
}
