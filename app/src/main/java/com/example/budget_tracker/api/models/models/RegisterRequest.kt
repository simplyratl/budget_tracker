package com.example.budget_tracker.api.models.models

data class RegisterRequest(
    val name: String,
    val email: String,
    val budget: Double,
    val password: String
)