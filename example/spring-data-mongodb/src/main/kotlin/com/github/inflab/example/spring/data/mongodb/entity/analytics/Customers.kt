package com.github.inflab.example.spring.data.mongodb.entity.analytics

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("customers")
data class Customers(
    @Id
    val id: String,
    val username: String,
    val name: String,
    val address: String,
    val birthDate: LocalDateTime,
    val email: String,
    val accounts: List<Int>,
)
