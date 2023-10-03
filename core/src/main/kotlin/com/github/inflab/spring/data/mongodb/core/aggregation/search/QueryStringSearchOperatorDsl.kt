package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure queryString search operator using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/queryString">queryString</a>
 */
@AggregationMarker
class QueryStringSearchOperatorDsl {
    private val document = Document()

    /**
     * The indexed field to search by default.
     * Atlas Search only searches the field in `defaultPath` if you omit the field to search in the query.
     *
     * @param path The indexed field to search by default.
     */
    fun defaultPath(path: String) {
        document["defaultPath"] = path
    }

    /**
     * The indexed field to search by default.
     * Atlas Search only searches the field in `defaultPath` if you omit the field to search in the query.
     *
     * @param path The indexed field to search by default.
     */
    fun defaultPath(path: KProperty<String?>) {
        document["defaultPath"] = path.toDotPath()
    }

    /**
     * The indexed field to search by default.
     * Atlas Search only searches the field in `defaultPath` if you omit the field to search in the query.
     *
     * @param path The indexed field to search by default.
     */
    @JvmName("defaultPathIterable")
    fun defaultPath(path: KProperty<Iterable<String?>?>) {
        document["defaultPath"] = path.toDotPath()
    }

    /**
     * Specifies one or more indexed fields and values to search.
     * Fields and values are colon-delimited.
     * For example, to search the plot field for the string baseball, use `plot:baseball`.
     *
     * @param configuration The configuration block for the [QueryStringQueryOptionDsl].
     */
    fun query(configuration: QueryStringQueryOptionDsl.() -> QueryStringQueryOptionDsl.Query) {
        val query = QueryStringQueryOptionDsl().configuration()
        document["query"] = query.toString()
    }

    /**
     * The score assigned to matching search results.
     * You can modify the default score using the following options:
     *
     * - boost: multiply the result score by the given number.
     * - constant: replace the result score with the given number.
     * - function: replace the result score using the given expression.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring/#std-label-scoring-ref">Score the Documents in the Results</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    internal fun build() = Document("queryString", document)
}
