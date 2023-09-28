package com.github.inflab.example.spring.data.mongodb.entity.airbnb

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Field

data class ListingsAndReviewsAddress(
    val street: String,
    val suburb: String,
    @Field("government_area")
    val governmentArea: String,
    val market: String,
    val country: String,
    @Field("country_code")
    val countryCode: String,
    val location: GeoJsonPoint,
)
