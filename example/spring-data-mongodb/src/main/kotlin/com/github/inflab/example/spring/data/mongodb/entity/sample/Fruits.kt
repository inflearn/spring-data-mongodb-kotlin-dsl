package com.github.inflab.example.spring.data.mongodb.entity.sample

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("fruits")
data class Fruits(
    @Id
    val id: String,
    val type: String?,
    val description: String,
    val quantities: FruitQuantities?,
)
