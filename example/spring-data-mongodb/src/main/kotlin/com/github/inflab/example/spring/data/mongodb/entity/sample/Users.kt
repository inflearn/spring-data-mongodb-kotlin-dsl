package com.github.inflab.example.spring.data.mongodb.entity.sample

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("users")
data class Users(
    @Id
    val id: String,
    val name: String,
    val verifiedUser: Boolean,
    val account: UserAccount,
    val teammates: List<String>,
    val region: String,
    val accountCreated: LocalDateTime,
    val employeeNumber: Int,
)
