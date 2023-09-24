package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import org.bson.Document
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure phrase search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/phrase">phrase</a>
 */
@AggregationMarker
class PhraseSearchOperatorDsl {
    private val document = Document()

    /**
     * Allowable distance between words in the query phrase.
     * Lower value allows less positional distance between the words and greater value allows more reorganization of the words and more distance between the words to satisfy the query.
     * The default is 0, meaning that words must be exactly in the same position as the query in order to be considered a match.
     * Exact matches are scored higher.
     */
    var slop: Int? = null
        set(value) {
            document["slop"] = value
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
    fun path(vararg path: KProperty<String>) {
        document["path"] = path.map { it.toDotPath() }.firstOrAll()
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

    internal fun build() = Document("phrase", document)
}
