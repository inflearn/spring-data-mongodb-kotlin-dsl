package com.github.inflab.example.spring.data.mongodb.entity.supplies

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("sales")
data class Sales(
    @Id
    val id: String,
    val saleDate: String,
    val items: List<SaleItem>,
    val storeLocation: String,
    val couponUsed: Boolean,
    val purchaseMethod: String,
)
