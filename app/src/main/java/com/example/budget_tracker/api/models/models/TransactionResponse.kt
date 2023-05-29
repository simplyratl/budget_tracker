package com.example.budget_tracker.api.models.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionResponse(
    val amount: Double,
    val createdAt: String,
    val id: String,
    val title: String,
    val userId: String?,
    val valid: Boolean,
    val items: List<Item>?,
    val address: String?,
    val addedIncome: Boolean?
): Parcelable