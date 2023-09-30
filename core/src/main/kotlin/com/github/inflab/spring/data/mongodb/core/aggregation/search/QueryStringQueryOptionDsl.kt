package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker

/**
 * A Kotlin DSL to configure queryString query option operator using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString/#options">queryString options</a>
 */
@AggregationMarker
class QueryStringQueryOptionDsl {
    var query: Query? = null

    class Query(val value: String, val field: String?) {
        override fun toString(): String {
            return when (field) {
                null -> value
                else -> "\"$field\":$value"
            }
        }
    }

    val WILDCARD = "*"
    val QUESTION = "?"

    fun text(value: String, field: String? = null): Query {
        val escaped = value.replace("*", "\\*").replace("?", "\\?")

        return Query("\"$escaped\"", field)
    }

    fun wildcard(value: String, field: String? = null): Query {
        return Query("\"$value\"", field)
    }

    fun regex(pattern: String, field: String? = null): Query {
        return Query("/$pattern/", field)
    }

    fun range(left: String, right: String, leftInclusion: Boolean = true, rightInclusion : Boolean = true, field:String? = null): Query {
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

        return Query("$leftBracket$leftExp TO $rightExp$rightBracket", field)
    }

    fun fuzzy(value: String, count: Int, field: String? = null): Query {
        return Query("\"$value\"~$count", field)
    }

    fun sub(query: Query): Query {
        return Query("(${query.value})", query.field)
    }

    fun not(query: Query): Query {
        return Query("NOT (${query.value})", query.field)
    }

    infix fun Query.and(query: Query): Query {
        return Query("$this AND $query", null)
    }

    infix fun Query.or(query: Query): Query {
        return Query("$this OR $query", null)
    }

    internal fun build(): String {
        return checkNotNull(query) { "query must not be null" }.toString()
    }
}