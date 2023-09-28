package com.github.inflab.example.spring.data.mongodb.entity.sample

import org.springframework.data.mongodb.core.mapping.Field

data class UserAccount(
    @Field("new_user")
    val newUser: Boolean,
    @Field("active_user")
    val activeUser: Boolean,
)
