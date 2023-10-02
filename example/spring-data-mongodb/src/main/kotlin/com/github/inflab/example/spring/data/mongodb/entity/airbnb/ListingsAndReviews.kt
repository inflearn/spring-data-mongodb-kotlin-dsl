package com.github.inflab.example.spring.data.mongodb.entity.airbnb

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("listingsAndReviews")
data class ListingsAndReviews(
    @Id
    val id: String,
    val name: String,
    @Field("property_type")
    val propertyType: String,
    val address: ListingsAndReviewsAddress,
)
