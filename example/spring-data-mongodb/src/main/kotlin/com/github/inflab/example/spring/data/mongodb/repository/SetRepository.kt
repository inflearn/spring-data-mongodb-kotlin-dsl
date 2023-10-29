package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.bson.Document
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregate
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Avg
import org.springframework.data.mongodb.core.aggregation.AccumulatorOperators.Sum
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

@Repository
class SetRepository(
    private val mongoTemplate: MongoTemplate,
) {

    data class Score(
        @Id val id: Long,
        val student: String,
        val homework: List<Long>,
        val quiz: List<Long>,
        val extraCredit: Long,
    )

    data class ScoreWithAvgQuiz(
        @Id val id: Long,
        val student: String,
        val homework: List<Long>,
        val quiz: List<Long>,
        val extraCredit: Long,
        val quizAverage: Long,
    )

    data class ScoreWithTotal(
        @Id val id: Long,
        val student: String,
        val homework: List<Long>,
        val quiz: List<Long>,
        val extraCredit: Long,
        val totalHomework: Long,
        val totalQuiz: Long,
        val totalScore: Long,
    )

    data class Specs(val doors: Long?, val wheels: Long?, @Field("fuel_type") val fuelType: String?)
    data class Vehicle(val id: Long, val type: String, val specs: Specs?)

    data class Animal(val id: Long, val dogs: Long, val cats: Long)

    data class Fruit(@Id val id: String, val item: String, val type: String)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#using-two--set-stages">Using Two $set Stages</a>
     */
    fun twoSetStages(): AggregationResults<ScoreWithTotal> {
        val aggregation = aggregation {
            set {
                // TODO: add $sum expression
                ScoreWithTotal::totalHomework set Sum.sumOf("homework")
                ScoreWithTotal::totalQuiz set Sum.sumOf("quiz")
            }
            set {
                ScoreWithTotal::totalScore set {
                    add { of(ScoreWithTotal::totalHomework) and ScoreWithTotal::totalQuiz and ScoreWithTotal::extraCredit }
                }
            }
        }

        return mongoTemplate.aggregate<ScoreWithTotal>(aggregation, SCORES)
    }

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

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#add-element-to-an-array">Add Element to an Array</a>
     */
    fun addElementToAnArray(): AggregationResults<Score> {
        val aggregation = aggregation {
            match(Criteria.where(Score::id.toDotPath()).isEqualTo(1))
            set {
                // TODO: add $concatArrays expression
                Score::homework set Document("\$concatArrays", listOf("\$${Score::homework.toDotPath()}", listOf(7)))
            }
        }

        return mongoTemplate.aggregate<Score>(aggregation, SCORES)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/set/#creating-a-new-field-with-existing-fields">Creating a New Field with Existing Fields</a>
     */
    fun createNewFieldWithExistingFields(): AggregationResults<ScoreWithAvgQuiz> {
        val aggregation = aggregation {
            set {
                // TODO: add $avg expression
                ScoreWithAvgQuiz::quizAverage set Avg.avgOf("quiz")
            }
        }

        return mongoTemplate.aggregate<ScoreWithAvgQuiz>(aggregation, SCORES)
    }

    companion object {
        const val SCORES = "scores"
        const val VEHICLES = "vehicles"
        const val ANIMALS = "animals"
        const val FRUITS = "fruits"
    }
}
