package com.example.budget_tracker.api.models.models

data class UserResponse(
    var budget: Double,
    val email: String,
    val id: String,
    val name: String
)