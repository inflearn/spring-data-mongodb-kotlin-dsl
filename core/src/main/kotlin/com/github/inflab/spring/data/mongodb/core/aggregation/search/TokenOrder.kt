package com.github.inflab.spring.data.mongodb.core.aggregation.search

/**
 * Order in which to search for tokens. Value can be one of the following
 * Default value is ANY
 */
enum class TokenOrder {

    /**
     * Indicates tokens in the query can appear in any order in the documents.
     * Results contain documents where the tokens appear sequentially and non-sequentially.
     * However, results where the tokens appear sequentially score higher than other, non-sequential values.
     */
    ANY,

    /**
     * Indicates tokens in the query must appear adjacent to each other or in the order specified in the query in the documents.
     * Results contain only documents where the tokens appear sequentially.
     */
    SEQUENTIAL,
}
