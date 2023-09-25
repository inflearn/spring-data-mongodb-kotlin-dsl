package com.github.inflab.spring.data.mongodb.core.extension

import com.github.inflab.spring.data.mongodb.core.mapping.asString
import kotlin.reflect.KProperty

/**
 * Extension for [KProperty] providing an `asString` function to render a [KProperty] in dot notation.
 *
 * @author Jake Son
 * @since 1.0
 */
fun KProperty<*>.toDotPath(): String = asString(this)
