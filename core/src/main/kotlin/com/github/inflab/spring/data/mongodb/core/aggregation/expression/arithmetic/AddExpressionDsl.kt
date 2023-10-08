package com.github.inflab.spring.data.mongodb.core.aggregation.expression.arithmetic

import com.github.inflab.spring.data.mongodb.core.aggregation.expression.AggregationExpressionDsl
import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import java.time.temporal.Temporal
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure `$add` [AggregationExpression] using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://docs.mongodb.com/manual/reference/operator/aggregation/add">$add</a>
 */
@AggregationMarker
class AddExpressionDsl {
    /**
     * Represents all operands inside `$add` as a list.
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
     * Creates a [Operands] with the given [date].
     *
     * @param date The date.
     */
    fun of(date: Temporal) = Operands(mutableListOf(date))

    /**
     * Creates a [Operands] with the given [field].
     *
     * @param field The name of the field.
     */
    fun of(field: String) = Operands(mutableListOf("$$field"))

    /**
     * Creates a [Operands] with the given [field].
     *
     * @param field The name of the field.
     */
    fun of(field: KProperty<*>) = of(field.toDotPath())

    /**
     * Creates a [Operands] with the given [AggregationExpression].
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    fun of(configuration: AggregationExpressionDsl.() -> AggregationExpression) =
        Operands(mutableListOf(AggregationExpressionDsl().configuration()))

    /**
     * Adds [value] to the list of operands.
     *
     * @param value The number.
     */
    infix fun Operands.and(value: Number): Operands {
        values.add(value)
        return this
    }

    /**
     * Adds [date] to the list of operands.
     *
     * @param date The date.
     */
    infix fun Operands.and(date: Temporal): Operands {
        values.add(date)
        return this
    }

    /**
     * Adds [field] to the list of operands.
     *
     * @param field The name of the field.
     */
    infix fun Operands.and(field: String): Operands {
        values.add("$$field")
        return this
    }

    /**
     * Adds [field] to the list of operands.
     *
     * @param field The name of the field.
     */
    infix fun Operands.and(field: KProperty<*>): Operands = and(field.toDotPath())

    /**
     * Adds [AggregationExpression] to the list of operands.
     *
     * @param configuration The configuration block for the [AggregationExpressionDsl].
     */
    infix fun Operands.and(configuration: AggregationExpressionDsl.() -> AggregationExpression): Operands {
        values.add(AggregationExpressionDsl().configuration())
        return this
    }

    internal fun build(configuration: AddExpressionDsl.() -> Operands) = AggregationExpression { context ->
        Document(
            "\$add",
            configuration().values.map {
                if (it is AggregationExpression) it.toDocument(context) else it
            },
        )
    }
}
