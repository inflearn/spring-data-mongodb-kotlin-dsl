package com.github.inflab.spring.data.mongodb.core.aggregation.search

/**
 * Aggregation strategies for combining scores of matching embedded documents.
 *
 * @author Jake Son
 * @since 1.0
 */
enum class ScoreEmbeddedAggregateStrategy {
    /**
     * Sum the score of all matching embedded documents.
     */
    SUM,

    /**
     * Choose the greatest score of all matching embedded documents.
     */
    MAXIMUM,

    /**
     * Choose the least high score of all matching embedded documents.
     */
    MINIMUM,

    /**
     * Choose the average (arithmetic mean) score of all matching embedded documents. Atlas Search includes scores of matching embedded documents only when computing the average. Atlas Search doesn't count embedded documents that don't satisfy query predicates as documents with scores of zero.
     */
    MEAN,
}
