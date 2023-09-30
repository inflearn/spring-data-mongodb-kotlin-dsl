package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure queryString query option operator using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#options">queryString options</a>
 */
@AggregationMarker
class QueryStringQueryOptionDsl {
    private lateinit var query: Query

    class Query(val value: String, val field: String?) {
        override fun toString(): String {
            return when (field) {
                null -> value
                else -> "$field:$value"
            }
        }
    }

    /**
     * Indicates 0 or more characters to match.
     */
    val WILDCARD = "*"

    /**
     * Indicates any single character to match.
     */
    val QUESTION = "?"

    /**
     * Creates a text query.
     *
     * @param value The value to search
     * @param field Indexed field search
     */
    fun text(value: String, field: String? = null): Query {
        val escaped = value.replace("*", "\\*").replace("?", "\\?")

        query = Query("\"$escaped\"", field)

        return query
    }

    /**
     * Creates a text query.
     *
     * @param value The value to search
     * @param field Indexed field search
     */
    fun text(value: String, field: KProperty<String?>): Query {
        val escaped = value.replace("*", "\\*").replace("?", "\\?")

        query = Query("\"$escaped\"", field.toDotPath())

        return query
    }

    /**
     * Creates a wildcard query.
     *
     * @param value The value to search
     * @param field Indexed field to search
     */
    fun wildcard(value: String, field: String? = null): Query {
        query = Query(value, field)

        return query
    }

    /**
     * Creates a wildcard query.
     *
     * @param value The value to search
     * @param field Indexed field to search
     */
    fun wildcard(value: String, field: KProperty<String?>): Query {
        query = Query(value, field.toDotPath())

        return query
    }

    /**
     * Creates a regex query.
     *
     * @param pattern The pattern to search
     * @param field Indexed field to search
     */
    fun regex(pattern: String, field: String? = null): Query {
        query = Query("/$pattern/", field)

        return query
    }

    /**
     * Creates a regex query.
     *
     * @param pattern The pattern to search
     * @param field Indexed field to search
     */
    fun regex(pattern: String, field: KProperty<String?>): Query {
        query = Query("/$pattern/", field.toDotPath())

        return query
    }

    /**
     * Creates a range query.
     *
     * @param left The left value to search
     * @param right The right value to search
     * @param leftInclusion The left value is included in the range
     * @param rightInclusion The right value is included in the range
     * @param field Indexed field to search
     */
    fun range(left: String, right: String, leftInclusion: Boolean = true, rightInclusion: Boolean = true, field: String? = null): Query {
        val leftBracket = if (leftInclusion) "[" else "{"
        val rightBracket = if (rightInclusion) "]" else "}"

        val leftExp = when (left) {
            WILDCARD -> WILDCARD
            QUESTION -> QUESTION
            else -> "\"$left\""
        }
        val rightExp = when (right) {
            WILDCARD -> WILDCARD
            QUESTION -> QUESTION
            else -> "\"$right\""
        }

        query = Query("$leftBracket$leftExp TO $rightExp$rightBracket", field)

        return query
    }

    /**
     * Creates a range query.
     *
     * @param left The left value to search
     * @param right The right value to search
     * @param leftInclusion The left value is included in the range
     * @param rightInclusion The right value is included in the range
     * @param field Indexed field to search
     */
    fun range(left: String, right: String, leftInclusion: Boolean = true, rightInclusion: Boolean = true, field: KProperty<String?>): Query {
        val leftBracket = if (leftInclusion) "[" else "{"
        val rightBracket = if (rightInclusion) "]" else "}"

        val leftExp = when (left) {
            WILDCARD -> WILDCARD
            QUESTION -> QUESTION
            else -> "\"$left\""
        }
        val rightExp = when (right) {
            WILDCARD -> WILDCARD
            QUESTION -> QUESTION
            else -> "\"$right\""
        }

        query = Query("$leftBracket$leftExp TO $rightExp$rightBracket", field.toDotPath())

        return query
    }

    /**
     * Creates a fuzzy query.
     *
     * @param value The value to search
     * @param maxEdits Maximum number of single-character edits required to match the specified search term.
     * @param field indexed field to search
     */
    fun fuzzy(value: String, maxEdits: Int, field: String? = null): Query {
        query = Query("$value~$maxEdits", field)

        return query
    }

    /**
     * Creates a fuzzy query.
     *
     * @param value The value to search
     * @param maxEdits Maximum number of single-character edits required to match the specified search term.
     * @param field indexed field to search
     */
    fun fuzzy(value: String, maxEdits: Int, field: KProperty<String?>): Query {
        query = Query("$value~$maxEdits", field.toDotPath())

        return query
    }

    /**
     * Creates a delimiters for subqueries.
     *
     * @param inputQuery The Query for subqueries.
     */
    fun sub(inputQuery: Query, field: String? = null): Query {
        query = Query("${field?.let { "$it:" }.orEmpty()}(${inputQuery.value})", inputQuery.field)

        return query
    }

    /**
     * Creates a delimiters for subqueries.
     *
     * @param inputQuery The Query for subqueries.
     */
    fun sub(inputQuery: Query, field: KProperty<String?>): Query {
        query = Query("${field.toDotPath().let { "$it:" }}(${inputQuery.value})", inputQuery.field)

        return query
    }

    /**
     * Creates an operator that indicates `NOT` boolean operator.
     * Specified search value must be absent for a document to be included in the results.
     *
     * @param inputQuery The Query to apply.
     */
    fun not(inputQuery: Query): Query {
        query = Query("NOT (${inputQuery.value})", inputQuery.field)

        return query
    }

    /**
     * Creates an operator that indicates `AND` boolean operator.
     * All search values must be present for a document to be included in the results.
     *
     * @param inputQuery The Query to apply.
     */
    infix fun Query.and(inputQuery: Query): Query {
        query = Query("$this AND $inputQuery", null)

        return query
    }

    /**
     * Creates an operator that indicates OR boolean operator.
     * At least one of the search value must be present for a document to be included in the results.
     *
     * @param inputQuery The Query to apply.
     */
    infix fun Query.or(inputQuery: Query): Query {
        query = Query("$this OR $inputQuery", null)

        return query
    }

    internal fun build(): String {
        return checkNotNull(query) { "query must not be null" }.toString()
    }
}
