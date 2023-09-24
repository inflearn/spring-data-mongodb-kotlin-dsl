package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import org.bson.Document
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure text search operator using idiomatic Kotlin code.
 *
 * @author Jake Son
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/text/#text">text</a>
 */
@AggregationMarker
class TextSearchOperatorDsl {
    private val document = Document()

    /**
     * The string or strings to search for.
     * If there are multiple terms in a string, Atlas Search also looks for a match for each term in the string separately.
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
     * Enable fuzzy search.
     * Find strings which are similar to the search term or terms.
     * You can't use fuzzy with synonyms.
     *
     * @param maxEdits Maximum number of single-character edits required to match the specified search term.
     * Value can be 1 or 2.
     * The default value is 2.
     * Uses [Damerau-Levenshtein distance](https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance).
     * @param prefixLength Number of characters at the beginning of each term in the result that must exactly match.
     * The default value is 0.
     * @param maxExpansions The maximum number of variations to generate and search for.
     * This limit applies on a per-token basis.
     * The default value is 50.
     */
    fun fuzzy(maxEdits: Int? = null, prefixLength: Int? = null, maxExpansions: Int? = null) {
        document["fuzzy"] = Document().apply {
            maxEdits?.let { put("maxEdits", it) }
            prefixLength?.let { put("prefixLength", it) }
            maxExpansions?.let { put("maxExpansions", it) }
        }
    }

    /**
     * The score assigned to matching search term results. Use one of the following options to modify the score:
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

    /**
     * Required for running queries using synonyms.
     * Name of the synonym mapping definition in the index definition.
     * Value can't be an empty string.
     * You can't use fuzzy with synonyms.
     *
     * @param value Name of the synonym mapping definition in the index definition.
     */
    fun synonyms(value: String) {
        document["synonyms"] = value
    }

    internal fun build() = Document("text", document)
}
