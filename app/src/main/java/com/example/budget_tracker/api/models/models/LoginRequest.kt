package com.example.budget_tracker.api.models.models

import android.provider.ContactsContract.CommonDataKinds.Email

data class LoginRequest(
    val email: String,
    val password: String
)
