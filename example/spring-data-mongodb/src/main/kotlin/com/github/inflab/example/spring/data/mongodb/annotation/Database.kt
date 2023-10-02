package com.github.inflab.example.spring.data.mongodb.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Database(val value: String)
