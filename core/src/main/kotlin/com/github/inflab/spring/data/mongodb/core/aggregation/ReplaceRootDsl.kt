package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation.ReplaceRootDocumentOperation
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure $replaceRoot stage using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot">$replaceRoot (aggregation)</a>
 */
@AggregationMarker
class ReplaceRootDsl {
    private var operation = ReplaceRootDocumentOperation()

    /**
     * Specifies the new root document.
     *
     * @param configuration The configuration for the [ReplacementDocumentDsl]
     */
    fun newRoot(configuration: ReplacementDocumentDsl.() -> Unit) {
        ReplacementDocumentDsl().configuration()
    }

    internal fun get() = operation

    /**
     * A Kotlin DSL to configure replacement document using idiomatic Kotlin code.
     */
    inner class ReplacementDocumentDsl(
        private val prefix: String = "",
    ) {

        /**
         * Adds new fields to documents with aggregation expression.
         *
         * @param configuration The configuration block where you can use DSL to define aggregation expression.
         */
        infix fun String.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.addPrefix())
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param path The path of the field to contain value to be added.
         */
        infix fun String.set(path: KProperty<Any?>) {
            operation = operation.andValue("${'$'}${path.toDotPath()}").`as`(this.addPrefix())
        }

        /**
         * Adds new fields to documents with a value.
         *
         * @param value The value of the field to add.
         */
        infix fun String.set(value: Any) {
            operation = operation.andValue(value).`as`(this.addPrefix())
        }

        /**
         * Adds nested new fields to documents.
         *
         * @param path The path of the field to contain value to be added.
         * @param configuration The configuration block for [ReplacementDocumentDsl]
         */
        fun nested(path: String, configuration: ReplacementDocumentDsl.() -> Unit) {
            ReplacementDocumentDsl(path.addPrefix()).configuration()
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param fieldPath The path of the field to contain value to be added.
         */
        infix fun String.setByField(fieldPath: String) {
            operation = operation.andValue("$$fieldPath").`as`(this.addPrefix())
        }

        /**
         * Adds new fields to documents with aggregation expression.
         *
         * @param configuration The configuration block where you can use DSL to define aggregation expression.
         */
        infix fun KProperty<*>.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.toDotPath().addPrefix())
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param path The path of the field to contain value to be added.
         */
        infix fun KProperty<*>.set(path: KProperty<Any?>) {
            operation = operation.andValue("${'$'}${path.toDotPath()}").`as`(this.toDotPath().addPrefix())
        }

        /**
         * Adds new fields to documents with a value.
         *
         * @param value The value of the field to add.
         */
        infix fun KProperty<*>.set(value: Any) {
            operation = operation.andValue(value).`as`(this.toDotPath().addPrefix())
        }

        /**
         * Adds nested new fields to documents.
         *
         * @param path The path of the field to contain value to be added.
         * @param configuration The configuration block for [ReplacementDocumentDsl]
         */
        fun nested(path: KProperty<*>, configuration: ReplacementDocumentDsl.() -> Unit) {
            ReplacementDocumentDsl(path.toDotPath().addPrefix()).configuration()
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param fieldPath The path of the field to contain value to be added.
         */
        infix fun KProperty<*>.setByField(fieldPath: String) {
            operation = operation.andValue("$$fieldPath").`as`(this.toDotPath().addPrefix())
        }

        /**
         * add prefix to the field name.
         */
        private fun String.addPrefix(): String {
            if (prefix.isEmpty()) {
                return this
            }

            return "$prefix.$this"
        }
    }
}
