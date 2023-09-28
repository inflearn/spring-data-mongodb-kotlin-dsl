package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure wildcard search operator using idiomatic Kotlin code.
 *
 * @author minwoo
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/wildcard">wildcard</a>
 */
@AggregationMarker
class WildcardSearchOperatorDsl {
    private val document = Document()

    /**
     * Must be set to true if the query is run against an analyzed field.
     */
    var allowAnalyzedField: Boolean = false
        set(value) {
            document["allowAnalyzedField"] = value
            field = value
        }


    /**
     * The string or strings to search for.
     *
     * @param query The string or strings to search for.
     */
    fun query(vararg query: String) {
        document["query"] = query.toList().firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: String) {
        document["path"] = path.toList().firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(vararg path: KProperty<String?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    @JvmName("pathIterable")
    fun path(vararg path: KProperty<Iterable<String?>?>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
    }

    /**
     * The indexed field or fields to search.
     * You can also specify a wildcard path to search.
     * See path construction for more information.
     *
     * @param configuration The configuration block for the [PathSearchOptionDsl].
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/path-construction/#std-label-ref-path">Path Construction</a>
     */
    fun path(configuration: PathSearchOptionDsl<String>.() -> Unit) {
        document["path"] = PathSearchOptionDsl<String>().apply(configuration).build()
    }

    /**
     * Score to assign to matching search results.
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

    internal fun build() = Document("wildcard", document)

}