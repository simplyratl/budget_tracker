package com.example.budget_tracker.api.models.models

data class AddTransationRequest(
    val amount: Double,
    val userId: String,
    val title: String,
    val items: List<Item>?,
    val address: String?
)
