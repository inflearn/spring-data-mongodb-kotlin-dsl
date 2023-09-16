package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure score function option using idiomatic Kotlin code.
 *
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#function">Score Function</a>
 * @author Jake Son
 * @since 1.0
 */
@AggregationMarker
class ScoreFunctionSearchOptionDsl {
    sealed interface Expression {
        fun toDocument(): Document

        data object Score : Expression {
            override fun toDocument() = Document("score", "relevance")
        }

        class Constant(private val value: Double) : Expression {
            override fun toDocument() = Document("constant", value)
        }

        class Path(private val value: String, private val undefined: Double? = null) : Expression {
            override fun toDocument() = Document(
                "path",
                Document("value", value).apply {
                    undefined?.let { put("undefined", it) }
                },
            )
        }

        class Gauss(
            private val decay: Double?,
            private val offset: Double?,
            private val origin: Double,
            private val path: Expression.Path,
            private val scale: Double,
        ) : Expression {
            override fun toDocument() = Document(
                "gauss",
                Document().apply {
                    decay?.let { put("decay", it) }
                    offset?.let { put("offset", it) }
                    put("origin", origin)
                    put("scale", scale)
                    put("path", path.toDocument()["path"])
                },
            )
        }

        class Add(private val expressions: MutableList<Expression>) : Expression {
            override fun toDocument() = Document("add", expressions.map { it.toDocument() })

            fun append(expression: Expression) { expressions.add(expression) }
        }

        class Multiply(private val expressions: MutableList<Expression>) : Expression {
            override fun toDocument() = Document("multiply", expressions.map { it.toDocument() })

            fun append(expression: Expression) { expressions.add(expression) }
        }

        class Log(private val expression: Expression) : Expression {
            override fun toDocument() = Document("log", expression.toDocument())
        }

        class Log1p(private val expression: Expression) : Expression {
            override fun toDocument() = Document("log1p", expression.toDocument())
        }
    }

    /**
     * Represents a final score of documents.
     */
    var expression: Expression? = null

    /**
     * Create an expression that represents the relevance score, which is the score Atlas Search assigns documents based on relevance, of the query.
     * It is the same as the current score of the document.
     */
    fun score() = Expression.Score

    /**
     * Create an expression that allows a [constant](https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#std-label-scoring-constant) number in the function score.
     * For example, you can use constant to modify relevance ranking through a numeric value from a data enrichment pipeline.
     *
     * @param value Number that indicates a fixed value. Atlas Search supports negative values.
     */
    fun constant(value: Double) = Expression.Constant(value)

    /**
     * Create an expression that incorporates an indexed numeric field value into a function score.
     *
     * @param value Name of numeric field. Field can contain negative numeric values.
     * @param undefined Value to use if the numeric field specified using value is missing in the document. If omitted, defaults to 0.
     */
    fun path(value: String, undefined: Double? = null) = Expression.Path(value, undefined)

    /**
     * Create an expression that incorporates an indexed numeric field value into a function score.
     *
     * @param value [KProperty] of numeric field. Field can contain negative numeric values.
     * @param undefined Value to use if the numeric field specified using value is missing in the document. If omitted, defaults to 0.
     */
    fun path(value: KProperty<*>, undefined: Double? = null) = Expression.Path(value.name, undefined)

    /**
     * Create an expression that allows you to decay, or reduce by multiplying, the final scores of the documents based on the distance of a numeric field value from a specified origin point.
     *
     * @param decay Rate at which you want to multiply the scores. Value must be a positive number between 0 and 1 exclusive. If omitted, defaults to 0.5.
     * For documents whose numeric field value (specified using path) is at a distance (specified using scale) away from origin plus or minus (±) offset, Atlas Search multiplies the current score using decay.
     * @param offset Number to use to determine the distance from origin. The decay operation is performed only for documents whose distances are greater than origin plus or minus (±) offset. If ommitted, defaults to 0.
     * @param origin Point of origin from which to calculate the distance.
     * @param path Name of the numeric field whose value you want to use to multiply the base score.
     * @param scale Distance from origin plus or minus (±) offset at which scores must be multiplied.
     */
    fun gauss(
        decay: Double? = null,
        offset: Double? = null,
        origin: Double,
        path: Expression.Path,
        scale: Double,
    ) = Expression.Gauss(decay, offset, origin, path, scale)

    /**
     * Create an expression that calculates the log10 of given expression.
     *
     * @param expression [Expression] to calculate the log10 of. If the specified expression evaluates to less than or equal to 0, then the log evaluates to undefined.
     */
    fun log(expression: Expression) = Expression.Log(expression)

    /**
     * Create an expression that calculates the log10(x+1) of given expression.
     *
     * @param expression [Expression] to calculate the log10(x+1) of.
     */
    fun log1p(expression: Expression) = Expression.Log1p(expression)

    /**
     * Create an expression that adds the results of multiple expressions.
     *
     * @param expressions Adds a series of numbers. Takes an array of [Expression], which can have negative values. Array length must be greater than or equal to 2.
     */
    fun add(vararg expressions: Expression) = Expression.Add(mutableListOf(*expressions))

    /**
     * Create an expression that multiplies the results of multiple expressions.
     *
     * @param expressions Multiplies a series of numbers. Takes an array of [Expression], which can have negative values. Array length must be greater than or equal to 2.
     */
    fun multiply(vararg expressions: Expression) = Expression.Multiply(mutableListOf(*expressions))

    internal fun build(): Document =
        checkNotNull(expression) { "Expression must not be null" }.toDocument()
}
