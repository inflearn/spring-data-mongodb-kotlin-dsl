package com.github.inflab.spring.data.mongodb.core.mapping

import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Field

internal class KPropertyPathTest : FreeSpec({
    data class Child(
        @Id val id: Long,
        val name: String,
        val names: List<String>,
        @Field(name = "real_name") val realName: String,
        @Field("real_names") val realNames: List<String>,
        @Field(order = 1) val order: Int,
    )

    data class Parent(@Field("x") @Id val id: Long, val child: Child, val children: List<Child>)
    data class GrandParent(val parent: Parent)

    "should return field name for a simple property" {
        // given
        val property = Child::name

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "name"
    }

    "should return field name for a property with @Field annotation" {
        // given
        val property = Child::realName

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "real_name"
    }

    "should return field name for a property with @Field annotation with value" {
        // given
        val property = Child::realNames

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "real_names"
    }

    "should return field name for a property with @Field annotation without name and value" {
        // given
        val property = Child::order

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "order"
    }

    "should return field name for a nested property" {
        // given
        val property = Parent::child..Child::name

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "child.name"
    }

    "should return field name for a nested property with @Field annotation" {
        // given
        val property = Parent::child..Child::realName

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "child.real_name"
    }

    "should return field name for a nested property in a list" {
        // given
        val property = Parent::children..Child::name

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "children.name"
    }

    "should return field name for a nested property in a list with @Field annotation" {
        // given
        val property = Parent::children..Child::realName

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "children.real_name"
    }

    "should return field name for a nested property in a nested property" {
        // given
        val property = GrandParent::parent..Parent::child..Child::name

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "parent.child.name"
    }

    "should return field name for a nested property in a nested property in a list" {
        // given
        val property = GrandParent::parent..Parent::children..Child::name

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "parent.children.name"
    }

    "should return _id for a property with @Id annotation" {
        // given
        val property = Child::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "_id"
    }

    "should return _id for a nested property with @Id annotation" {
        // given
        val property = Parent::child..Child::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "child._id"
    }

    "should return _id for a named id property with @Id and @Field annotation" {
        // given
        val property = Parent::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "_id"
    }

    "should return _id for a named id property without annotation" {
        // given
        data class Test(val id: Long)
        val property = Test::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "_id"
    }

    "should return _id for a named id property with empty @Field annotation" {
        // given
        data class Test(@Field val id: Long)
        val property = Test::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "_id"
    }

    "should return field name for a named id property with @Field annotation" {
        // given
        data class Test(@Field("x") val id: Long)
        val property = Test::id

        // when
        val actual = property.toDotPath()

        // then
        actual shouldBe "x"
    }
})
