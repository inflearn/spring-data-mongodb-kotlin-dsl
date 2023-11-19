package com.github.inflab.spring.data.mongodb.core.aggregation.expression.comparison

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$gte` [GteExpressionDsl] using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/gte">$gte</a>
 */
@AggregationMarker
class GteExpressionDsl {
    /**
     * Represents all operands inside `$gte` as a list.
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
    @JvmName("ofString")
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
    infix fun Operands.greaterThanEqual(value: Number): Operands {
        values.add(value)
        return this
    }

    /**
     * Compares [field] and returns true or false.
     *
     * @param field The name of the field.
     */
    infix fun Operands.greaterThanEqual(field: String): Operands {
        values.add("$$field")
        return this
    }

    /**
     * Compares [property] and returns true or false.
     *
     * @param property The name of the field.
     */
    infix fun Operands.greaterThanEqual(property: KProperty<Number?>): Operands {
        greaterThanEqual(property.toDotPath())
        return this
    }

    /**
     * Compares [property] and returns true or false.
     *
     * @param property The name of the field.
     */
    @JvmName("greaterThanEqualString")
    infix fun Operands.greaterThanEqual(property: KProperty<String?>): Operands {
        greaterThanEqual(property.toDotPath())
        return this
    }

    /**
     * Compares [AggregationExpression] and returns true or false.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Operands.greaterThanEqual(configuration: AggregationExpressionDsl.() -> AggregationExpression): Operands {
        values.add(AggregationExpressionDsl().configuration())
        return this
    }

    internal fun build(configuration: GteExpressionDsl.() -> Operands) = AggregationExpression { context ->
        Document(
            "\$gte",
            configuration().values.map {
                if (it is AggregationExpression) it.toDocument(context) else it
            },
        )
    }
}
