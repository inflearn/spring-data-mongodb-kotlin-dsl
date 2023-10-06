package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure score function option using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score/#function">Score Function</a>
 */
@AggregationMarker
class ScoreFunctionSearchOptionDsl {
    /**
     * Represents an expression that can be used in a score function.
     */
    sealed interface Expression {
        /**
         * Converts this expression to a [Document].
         */
        fun toDocument(): Document

        /**
         * Represents the relevance score, which is the score Atlas Search assigns documents based on relevance, of the query.
         */
        data object Score : Expression {
            override fun toDocument() = Document("score", "relevance")
        }

        /**
         * Represents a constant number in the function score.
         */
        class Constant(private val value: Number) : Expression {
            override fun toDocument() = Document("constant", value)
        }

        /**
         * Represents an indexed numeric field value in the function score.
         */
        class Path(private val value: String, private val undefined: Number? = null) : Expression {
            override fun toDocument() = Document(
                "path",
                Document("value", value).apply {
                    undefined?.let { put("undefined", it) }
                },
            )
        }

        /**
         * Represents a decayed score based on the distance of a numeric field value from a specified origin point.
         */
        class Gauss(
            private val decay: Number?,
            private val offset: Number?,
            private val origin: Number,
            private val path: Path,
            private val scale: Number,
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

        /**
         * Represents an expression that adds the results of multiple expressions.
         */
        class Add(private val expressions: MutableList<Expression>) : Expression {
            override fun toDocument() = Document("add", expressions.map { it.toDocument() })

            /**
             * Appends an expression to add.
             */
            fun append(expression: Expression) { expressions.add(expression) }
        }

        /**
         * Represents an expression that multiplies the results of multiple expressions.
         */
        class Multiply(private val expressions: MutableList<Expression>) : Expression {
            override fun toDocument() = Document("multiply", expressions.map { it.toDocument() })

            /**
             * Appends an expression to multiply.
             */
            fun append(expression: Expression) { expressions.add(expression) }
        }

        /**
         * Represents an expression that calculates the log10 of given expression.
         */
        class Log(private val expression: Expression) : Expression {
            override fun toDocument() = Document("log", expression.toDocument())
        }

        /**
         * Represents an expression that calculates the log10(x+1) of given expression.
         */
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
    fun constant(value: Number) = Expression.Constant(value)

    /**
     * Create an expression that incorporates an indexed numeric field value into a function score.
     *
     * @param value Name of numeric field. Field can contain negative numeric values.
     * @param undefined Value to use if the numeric field specified using value is missing in the document. If omitted, defaults to 0.
     */
    fun path(value: String, undefined: Number? = null) = Expression.Path(value, undefined)

    /**
     * Create an expression that incorporates an indexed numeric field value into a function score.
     *
     * @param value [KProperty] of numeric field. Field can contain negative numeric values.
     * @param undefined Value to use if the numeric field specified using value is missing in the document. If omitted, defaults to 0.
     */
    fun path(value: KProperty<Number?>, undefined: Number? = null) = Expression.Path(value.toDotPath(), undefined)

    /**
     * Create an expression that allows you to decay, or reduce by multiplying, the final scores of the documents based on the distance of a numeric field value from a specified origin point.
     *
     * @param decay Rate at which you want to multiply the scores. Value must be a positive number between 0 and 1 exclusive.
     * If omitted, defaults to 0.5.
     * For documents whose numeric field value (specified using path) is at a distance (specified using scale) away from origin plus or minus (±) offset, Atlas Search multiplies the current score using decay.
     * @param offset Number to use to determine the distance from origin.
     * The decay operation is performed only for documents whose distances are greater than origin plus or minus (±) offset.
     * If ommitted, defaults to 0.
     * @param origin Point of origin from which to calculate the distance.
     * @param path Name of the numeric field whose value you want to use to multiply the base score.
     * @param scale Distance from origin plus or minus (±) offset at which scores must be multiplied.
     */
    fun gauss(
        decay: Number? = null,
        offset: Number? = null,
        origin: Number,
        path: Expression.Path,
        scale: Number,
    ) = Expression.Gauss(decay, offset, origin, path, scale)

    /**
     * Create an expression that calculates the log10 of given expression.
     *
     * @param expression [Expression] to calculate the log10 of.
     * If the specified expression evaluates to less than or equal to 0, then the log evaluates to undefined.
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
     * @param expressions Adds a series of numbers.
     * Takes an array of [Expression], which can have negative values.
     * Array length must be greater than or equal to 2.
     */
    fun add(vararg expressions: Expression) = Expression.Add(expressions.toMutableList())

    /**
     * Create an expression that multiplies the results of multiple expressions.
     *
     * @param expressions Multiplies a series of numbers.
     * Takes an array of [Expression], which can have negative values.
     * Array length must be greater than or equal to 2.
     */
    fun multiply(vararg expressions: Expression) = Expression.Multiply(expressions.toMutableList())
}
