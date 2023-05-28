package com.example.budget_tracker.api.models.models

data class TransactionResponse(
    val amount: Double,
    val createdAt: String,
    val id: String,
    val title: String,
    val userId: String,
    val valid: Boolean
)