package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import com.github.inflab.spring.data.mongodb.core.extension.firstOrAll
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import org.bson.Document
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to build autocomplete search operator using idiomatic Kotlin code.
 *
 * @author username1103
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/autocomplete">autocomplete</a>
 */
@AggregationMarker
class AutocompleteSearchOperationDsl {
    private val document = Document()

    /**
     * The string or strings to search for.
     * If there are multiple terms in a string, Atlas Search also looks for a match for each term in the string separately.
     *
     * @param query The string or strings to search for.
     */
    fun query(vararg query: String) {
        document["query"] = query.toList().firstOrAll()
    }

    /**
     * The string or strings to search for.
     * If there are multiple terms in a string, Atlas Search also looks for a match for each term in the string separately.
     *
     * @param query The string or strings to search for.
     */
    fun query(query: Iterable<String>) {
        document["query"] = query.toList().firstOrAll()
    }

    /**
     * Indexed autocomplete type of field to search.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/autocomplete-type/#std-label-bson-data-types-autocomplete">autocomplete</a>
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Indexed autocomplete type of field to search.
     *
     * @param path The indexed field or fields to search.
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/field-types/autocomplete-type/#std-label-bson-data-types-autocomplete">autocomplete</a>
     */
    fun path(path: KProperty<String?>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Enable fuzzy search.
     * Find strings which are similar to the search term or terms.
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
     *  @param scoreConfiguration The configuration block for [ScoreSearchOptionDsl]
     *  @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/score/modify-score">Modify the Score</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    /**
     * Order in which to search for tokens.
     * The default value is [TokenOrder.ANY]
     *
     * @param tokenOrder Order in which to search for tokens. [TokenOrder]
     */
    fun tokenOrder(tokenOrder: TokenOrder) {
        document["tokenOrder"] = tokenOrder.name.lowercase()
    }

    internal fun build(): Document = Document("autocomplete", document)
}
