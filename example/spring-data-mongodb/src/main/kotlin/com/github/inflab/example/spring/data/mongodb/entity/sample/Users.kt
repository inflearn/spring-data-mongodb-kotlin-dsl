package com.github.inflab.example.spring.data.mongodb.entity.sample

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document("users")
data class Users(
    @Id
    val id: String,
    val name: String,
    @Field("verified_user")
    val verifiedUser: Boolean,
    val account: UserAccount,
    val teammates: List<String>,
    val region: String,
    @Field("account_created")
    val accountCreated: LocalDateTime,
    @Field("employee_number")
    val employeeNumber: Int,
)
