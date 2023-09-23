package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.springframework.data.mapping.toDotPath
import org.springframework.data.mongodb.core.aggregation.Aggregation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $project stage using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/project">$project (aggregation)</a>
 */
@AggregationMarker
class ProjectStageDsl {
    private var operation = Aggregation.project()

    /**
     * Specifies the inclusion of a field.
     */
    operator fun String.unaryPlus() {
        operation = operation.andInclude(this)
    }

    /**
     * Specifies the inclusion of a field.
     */
    operator fun KProperty<*>.unaryPlus() {
        operation = operation.andInclude(this.toDotPath())
    }

    /**
     * Specifies the exclusion of a field.
     */
    operator fun String.unaryMinus() {
        operation = operation.andExclude(this)
    }

    /**
     * Specifies the exclusion of a field.
     */
    operator fun KProperty<*>.unaryMinus() {
        operation = operation.andExclude(this.toDotPath())
    }

    /**
     * Specifies the suppression of the _id field.
     */
    fun excludeId() {
        operation = operation.andExclude("_id")
    }

    /**
     * Specifies a projection field with the given [alias].
     *
     * @param alias The alias for the field.
     */
    infix fun String.alias(alias: String) {
        operation = operation.and(this).`as`(alias)
    }

    /**
     * Specifies a projection field with the given [alias].
     *
     * @param alias The alias for the field.
     */
    infix fun KProperty<*>.alias(alias: String) {
        operation = operation.and(this.toDotPath()).`as`(alias)
    }

    internal fun get() = operation
}
