package com.github.inflab.spring.data.mongodb.core.extension

/**
 * Returns the first element if the list has only one element, otherwise returns the list itself.
 *
 * @author Jake Son
 * @since 1.0
 */
internal fun List<String>.firstOrAll(): Any {
    return if (size == 1) first() else this
}
