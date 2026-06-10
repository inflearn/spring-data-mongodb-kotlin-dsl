package com.github.inflab.spring.data.mongodb.core.aggregation.search

/**
 * Defines the criteria to use to match the terms in the query.
 * The default value is [TextMatchCriteria.ANY].
 *
 * @author username1103
 * @since 1.0
 */
enum class TextMatchCriteria {

    /**
     * Indicates that Atlas Search returns documents that contain any of the terms from the query field.
     */
    ANY,

    /**
     * Indicates that Atlas Search returns documents that contain all of the terms from the query field.
     */
    ALL,
}
