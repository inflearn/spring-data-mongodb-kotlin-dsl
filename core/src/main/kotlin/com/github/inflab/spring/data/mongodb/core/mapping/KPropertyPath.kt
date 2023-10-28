package com.github.inflab.spring.data.mongodb.core.mapping

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.javaField

/**
 * Abstraction of a property path consisting of [KProperty].
 *
 * @author Jake Son
 * @since 1.0
 */
private class KPropertyPath<T, U>(
    val parent: KProperty<U?>,
    val child: KProperty1<U, T>,
) : KProperty<T> by child

/**
 * Recursively construct field name for a nested property.
 *
 * @author Jake Son
 * @since 1.0
 */
internal fun asString(property: KProperty<*>): String =
    if (property is KPropertyPath<*, *>) {
        "${asString(property.parent)}.${toFieldName(property.child)}"
    } else {
        toFieldName(property)
    }

/**
 * Get field name from [Field] annotation or property name.
 *
 * @param property property to get field name from
 * @author Jake Son
 * @since 1.0
 */
internal fun toFieldName(property: KProperty<*>): String {
    val idAnnotation = property.javaField?.getAnnotation(Id::class.java)
    if (idAnnotation != null) {
        return "_id"
    }

    val fieldAnnotation = property.javaField?.getAnnotation(Field::class.java) ?: return property.name

    return fieldAnnotation.value.ifEmpty { fieldAnnotation.name.ifEmpty { property.name } }
}

/**
 * Builds [KPropertyPath] from Property References.
 * Refer to a nested property in an embeddable or association.
 *
 * For example, referring to the field "author.name":
 * ```
 * Book::author..Author::name isEqualTo "Herman Melville"
 * ```
 * @author Jake Son
 * @since 1.0
 */
operator fun <T, U> KProperty<T?>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
    KPropertyPath(this, other)

/**
 * Builds [KPropertyPath] from Property References.
 * Refer to a nested property in an embeddable or association.
 *
 * For example, referring to the field "authors.name":
 * ```
 * Book::authors..Author::name isEqualTo "Herman Melville"
 * ```
 * @author Jake Son
 * @since 1.0
 */
@JvmName("iterableRangeTo")
operator fun <T, U> KProperty<Iterable<T>?>.rangeTo(other: KProperty1<T, U>): KProperty<U> =
    @Suppress("UNCHECKED_CAST")
    KPropertyPath(parent = this as KProperty<T?>, child = other)
