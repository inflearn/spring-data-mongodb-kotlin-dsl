package com.github.inflab.spring.data.mongodb.core.aggregation

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation.ReplaceRootDocumentOperation
import kotlin.reflect.KProperty

@AggregationMarker
class ReplaceRootDsl {
    private var operation = ReplaceRootDocumentOperation()

    fun newRoot(configuration: NewRootDsl.() -> Unit) {
        NewRootDsl().configuration()
    }

    internal fun get() = operation

    inner class NewRootDsl(
        private val prefix: String = "",
    ) {

        infix fun String.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.addPrefix())
        }

        infix fun String.set(path: KProperty<Any?>) {
            operation = operation.andValue("${'$'}${path.toDotPath()}").`as`(this.addPrefix())
        }

        infix fun String.set(value: Any) {
            operation = operation.andValue(value).`as`(this.addPrefix())
        }

        fun nested(path: String, configuration: NewRootDsl.() -> Void) {
            NewRootDsl(path).configuration()
        }

        infix fun String.setByField(fieldPath: String) {
            operation = operation.andValue("$$fieldPath").`as`(this.addPrefix())
        }

        infix fun KProperty<*>.set(configuration: AggregationExpressionDsl.() -> AggregationExpression) {
            operation = operation.and(AggregationExpressionDsl().configuration()).`as`(this.toDotPath().addPrefix())
        }

        infix fun KProperty<*>.set(path: KProperty<Any?>) {
            operation = operation.andValue("${'$'}${path.toDotPath()}").`as`(this.toDotPath().addPrefix())
        }

        infix fun KProperty<*>.set(value: Any) {
            operation = operation.andValue(value).`as`(this.toDotPath().addPrefix())
        }

        infix fun KProperty<*>.setByField(fieldPath: String) {
            operation = operation.andValue("$$fieldPath").`as`(this.toDotPath().addPrefix())
        }

        private fun String.addPrefix(): String {
            if (prefix.isEmpty()) {
                return this
            }

            return "$prefix.$this"
        }
    }
}
