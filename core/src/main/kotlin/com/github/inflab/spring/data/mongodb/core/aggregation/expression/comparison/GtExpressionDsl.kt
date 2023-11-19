package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$gt` [GtExpressionDsl] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/gt">$gt</a>
 */
@AggregationMarker
class GtExpressionDsl {
    /**
     * Represents all operands inside `$gt` as a list.
     *
     * @property values The list that contains all operands.
     */
    @JvmInline
    value class Operands(val values: MutableList<Any>)

    /**
     * Creates a [Operands] with the given [value].
     *
     * @param value The number.
     */
    fun of(value: Number) = Operands(mutableListOf(value))

    /**
     * Creates a [Operands] with the given [field].
     *
     * @param field The name of the field.
     */
    fun of(field: String) = Operands(mutableListOf("$$field"))

    /**
     * Creates a [Operands] with the given [property].
     *
     * @param property The name of the field.
     */
    fun of(property: KProperty<Number?>) = of(property.toDotPath())

    /**
     * Creates a [Operands] with the given [property].
     *
     * @param property The name of the field.
     */
    @JvmName("greaterThanString")
    fun of(property: KProperty<String?>) = of(property.toDotPath())

    /**
     * Creates a [Operands] with the given [AggregationExpression].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun of(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Operands(mutableListOf(AggregationExpressionDsl().configuration()))

    /**
     * Compares [value] and returns true or false.
     *
     * @param value The value to compare.
     */
    infix fun Operands.greaterThan(value: Number): Operands {
        values.add(value)
        return this
    }

    /**
     * Compares [field] and returns true or false.
     *
     * @param field The name of the field.
     */
    infix fun Operands.greaterThan(field: String): Operands {
        values.add("$$field")
        return this
    }

    /**
     * Compares [property] and returns true or false.
     *
     * @param property The name of the field.
     */
    infix fun Operands.greaterThan(property: KProperty<Number?>): Operands {
        greaterThan(property.toDotPath())
        return this
    }

    /**
     * Compares [property] and returns true or false.
     *
     * @param property The name of the field.
     */
    @JvmName("greaterThanString")
    infix fun Operands.greaterThan(property: KProperty<String?>): Operands {
        greaterThan(property.toDotPath())
        return this
    }

    /**
     * Compares [AggregationExpression] and returns true or false.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Operands.greaterThan(configuration: AggregationExpressionDsl.() -> AggregationExpression): Operands {
        values.add(AggregationExpressionDsl().configuration())
        return this
    }

    internal fun build(configuration: GtExpressionDsl.() -> Operands) = AggregationExpression { context ->
        Document(
            "\$gt",
            configuration().values.map {
                if (it is AggregationExpression) it.toDocument(context) else it
            },
        )
    }
}