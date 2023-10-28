package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.stereotype.Repository

@Repository
class SetRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Specs(val doors: Long?, val wheels: Long?, @Field("fuel_type") val fuelType: String?)
    data class Vehicle(val id: Long, val type: String, val specs: Specs?)

    data class Animal(val id: Long, val dogs: Long, val cats: Long)

    data class Fruit(@Id val id: String, val item: String, val type: String)

    // TODO: https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#using-two--set-stages

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#adding-fields-to-an-embedded-document">Adding Fields to an Embedded Document</a>
     */
    fun setToEmbeddedDocument(): AggregationResults<Vehicle> {
        val aggregation = aggregation {
            set {
                Vehicle::specs..Specs::fuelType set "unleaded"
            }
        }

        return mongoTemplate.aggregate<Vehicle>(aggregation, VEHICLES)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#overwriting-an-existing-field">Overwriting an existing field</a>
     */
    fun overwriteAnExistingFieldWithValue(): AggregationResults<Animal> {
        val aggregation = aggregation {
            set {
                Animal::cats set 20
            }
        }

        return mongoTemplate.aggregate<Animal>(aggregation, ANIMALS)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#overwriting-an-existing-field">Overwriting an existing field</a>
     */
    fun overwriteAnExistingFieldWithField(): AggregationResults<Fruit> {
        val aggregation = aggregation {
            set {
                Fruit::id set Fruit::item
                Fruit::item set "fruit"
            }
        }

        return mongoTemplate.aggregate<Fruit>(aggregation, FRUITS)
    }

    // TODO: https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#add-element-to-an-array

    // TODO: https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#creating-a-new-field-with-existing-fields

    companion object {
        const val VEHICLES = "vehicles"
        const val ANIMALS = "animals"
        const val FRUITS = "fruits"
    }
}
