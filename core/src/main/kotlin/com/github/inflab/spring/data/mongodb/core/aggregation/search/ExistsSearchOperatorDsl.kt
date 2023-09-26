package com.github.inflab.spring.data.mongodb.core.aggregation.search

import com.github.inflab.spring.data.mongodb.core.annotation.AggregationMarker
import org.bson.Document
import org.springframework.data.mapping.toDotPath
import kotlin.reflect.KProperty

/**
 * A Kotlin DSL to configure the exists operator using idiomatic Kotlin code.
 *
 * @author Cranemont
 * @since 1.0
 * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/exists/#exists">exists</a>
 */
@AggregationMarker
class ExistsSearchOperatorDsl {
    private val document = Document()

    /**
     * Indexed field to search.
     *
     * @param path The indexed field to search.
     */
    fun path(path: String) {
        document["path"] = path
    }

    /**
     * Indexed field to search.
     *
     * @param path The indexed field to search.
     */
    fun path(path: KProperty<*>) {
        document["path"] = path.toDotPath()
    }

    /**
     * Score to assign to matching search results.
     * To learn more about the options to modify the default score, see Score the Documents in the Results.
     *
     * @see <a href="https://www.mongodb.com/docs/atlas/atlas-search/scoring/#std-label-scoring-ref">Score the Documents in the Results</a>
     */
    fun score(scoreConfiguration: ScoreSearchOptionDsl.() -> Unit) {
        document["score"] = ScoreSearchOptionDsl().apply(scoreConfiguration).get()
    }

    internal fun build() = Document("exists", document)
}
