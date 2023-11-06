package com.github.inflab.example.spring.data.mongodb.repository

import com.github.inflab.spring.data.mongodb.core.aggregation.aggregation
import com.github.inflab.spring.data.mongodb.core.extension.toDotPath
import com.github.inflab.spring.data.mongodb.core.mapping.rangeTo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.AggregationExpression
import org.springframework.data.mongodb.core.aggregation.AggregationResults
import org.springframework.data.mongodb.core.aggregation.ObjectOperators.MergeObjects
import org.springframework.data.mongodb.core.aggregation.StringOperators.Concat
import org.springframework.data.mongodb.core.aggregation.SystemVariable
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.stereotype.Repository

@Repository
class ReplaceRootRepository(
    private val mongoTemplate: MongoTemplate,
) {
    data class Pet(val dogs: Long, val cats: Long, val birds: Long, val fish: Long)
    data class Student(@Id val id: Long, val grades: List<Grade>)
    data class Grade(val test: Long, val grade: Long, val mean: Long, val std: Long)
    data class Contact(@Id val id: Long, @Field("first_name") val firstName: String, @Field("last_name") val lastName: String, val city: String)
    data class FindFullNameResult(@Field("full_name") val fullName: String)
    data class ContactInfo(@Id val id: Long, val name: String, val email: String?, val cell: String?, val home: String?)

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot/#-replaceroot-with-an-embedded-document-field">ReplaceRoot with an Embedded Document Field</a>
     */
    fun replaceRootWithEmbeddedDocumentField(): AggregationResults<Pet> {
        val aggregation = aggregation {
            replaceRoot {
                newRoot<AggregationExpression> {
                    // TODO: add $mergeObjects expressions
                    MergeObjects.merge(
                        mapOf(
                            "dogs" to 0,
                            "cats" to 0,
                            "birds" to 0,
                            "fish" to 0,
                        ),
                    ).mergeWith("\$pets")
                }
            }
        }

        return mongoTemplate.aggregate(aggregation, PEOPLE, Pet::class.java)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot/#-replaceroot-with-a-document-nested-in-an-array">ReplaceRoot with a Document Nested in an Array</a>
     */
    fun replaceRootWithNestedArray(): AggregationResults<Grade> {
        val aggregation = aggregation {
            unwind { path(Student::grades) }
            // TODO: add $gte operators
            match(Criteria.where((Student::grades..Grade::grade).toDotPath()).gte(90))
            replaceRoot {
                newRoot(Student::grades)
            }
        }

        return mongoTemplate.aggregate(aggregation, STUDENT, Grade::class.java)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot/#-replaceroot-with-a-newly-created-document">ReplaceRoot with a Newly Created Document</a>
     */
    fun replaceRootWithNewlyCreatedDocument(): AggregationResults<FindFullNameResult> {
        val aggregation = aggregation {
            replaceRoot {
                newRoot {
                    FindFullNameResult::fullName set {
                        // TODO: add $concat expressions
                        Concat.valueOf(Contact::firstName.toDotPath()).concat(" ").concatValueOf(Contact::lastName.toDotPath())
                    }
                }
            }
        }

        return mongoTemplate.aggregate(aggregation, CONTACT, FindFullNameResult::class.java)
    }

    /**
     * @see <a href="https://www.mongodb.com/docs/manual/reference/operator/aggregation/replaceRoot/#-replaceroot-with-a-new-document-created-from---root-and-a-default-document">ReplaceRoot with a New Document Created from $$ROOT and a Default Document</a>
     */
    fun replaceRootWithNewlyCreatedDocumentAndDefaultDocument(): AggregationResults<ContactInfo> {
        val aggregation = aggregation {
            replaceRoot {
                newRoot<AggregationExpression> {
                    // TODO: add $mergeObjects expressions
                    MergeObjects.merge(
                        mapOf(
                            ContactInfo::id.toDotPath() to "",
                            ContactInfo::name.toDotPath() to "",
                            ContactInfo::email.toDotPath() to "",
                            ContactInfo::cell.toDotPath() to "",
                            ContactInfo::home.toDotPath() to "",
                        ),
                    ).mergeWith(SystemVariable.ROOT)
                }
            }
        }

        return mongoTemplate.aggregate(aggregation, CONTACT, ContactInfo::class.java)
    }

    companion object {
        const val PEOPLE = "people"
        const val STUDENT = "students"
        const val CONTACT = "contacts"
    }
}
