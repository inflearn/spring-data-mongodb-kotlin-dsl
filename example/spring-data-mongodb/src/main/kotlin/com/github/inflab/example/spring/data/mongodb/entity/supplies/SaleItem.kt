package com.github.inflab.example.spring.data.mongodb.entity.supplies

data class SaleItem(
    val name: String,
    val tags: List<String>,
    val price: Double,
    val quantity: Int,
)
