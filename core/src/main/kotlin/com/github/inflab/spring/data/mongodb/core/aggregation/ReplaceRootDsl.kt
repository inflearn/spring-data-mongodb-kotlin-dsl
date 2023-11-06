package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation
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
    private var operation: ReplaceRootOperation? = null

    /**
     * Specifies the new root document with document operation.
     *
     * @param configuration The configuration block for the [ReplacementDocumentOperationDsl].
     */
    fun newRoot(configuration: ReplacementDocumentOperationDsl.() -> Unit) {
        operation = ReplacementDocumentOperationDsl().apply(configuration).get()
    }

    /**
     * Specifies field name for the new root document.
     *
     * @param path The path of the field to replace new root.
     */
    fun newRoot(path: String) {
        operation = ReplaceRootOperation.builder().withValueOf(path)
    }

    /**
     * Specifies field name for the new root document.
     *
     * @param path The path of the field to replace new root.
     */
    fun newRoot(path: KProperty<*>) {
        operation = ReplaceRootOperation.builder().withValueOf(path.toDotPath())
    }

    /**
     * Specifies the new root document with aggregation expression.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun expressions(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
        operation = ReplaceRootOperation.builder().withValueOf(AggregationExpressionDsl().configuration())
    }

    internal fun get() = checkNotNull(operation) { "ReplaceRoot operation must not be null!" }

    /**
     * A Kotlin DSL to configure replacement document using idiomatic Kotlin code.
     */
    class ReplacementDocumentOperationDsl(
        private val prefix: String = "",
        private var operation: ReplaceRootDocumentOperation = ReplaceRootDocumentOperation(),
    ) {

        /**
         * Adds new fields to documents with aggregation expression.
         *
         * @param configuration The configuration block for the [AggregationExpressionDsl].
         */
        infix fun String.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.addPrefix())
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param path The path of the field to contain value to be added.
         */
        infix fun String.set(path: KProperty<*>) {
            operation = operation.andValue("$${path.toDotPath()}").`as`(this.addPrefix())
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
         * @param configuration The configuration block for [ReplacementDocumentOperationDsl].
         */
        fun nested(path: String, configuration: ReplacementDocumentOperationDsl.() -> Unit) {
            operation = ReplacementDocumentOperationDsl(path.addPrefix(), operation).apply(configuration).get()
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
         * @param configuration The configuration block for the [AggregationExpressionDsl].
         */
        infix fun KProperty<*>.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.toDotPath().addPrefix())
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param path The path of the field to contain value to be added.
         */
        infix fun KProperty<*>.set(path: KProperty<*>) {
            operation = operation.andValue("$${path.toDotPath()}").`as`(this.toDotPath().addPrefix())
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
         * @param configuration The configuration block for [ReplacementDocumentOperationDsl].
         */
        fun nested(path: KProperty<*>, configuration: ReplacementDocumentOperationDsl.() -> Unit) {
            operation = ReplacementDocumentOperationDsl(
                path.toDotPath().addPrefix(),
                operation,
            ).apply(configuration).get()
        }

        /**
         * Adds new fields to documents from a field.
         *
         * @param fieldPath The path of the field to contain value to be added.
         */
        infix fun KProperty<*>.setByField(fieldPath: String) {
            operation = operation.andValue("$$fieldPath").`as`(this.toDotPath().addPrefix())
        }

        private fun String.addPrefix(): String {
            if (prefix.isEmpty()) {
                return this
            }

            return "$prefix.$this"
        }

        internal fun get() = operation
    }
}
