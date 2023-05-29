package com.example.budget_tracker.api.models.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScannedQRCodeResponse(
    val adress: String,
    val amount: Double,
    val createdAt: String,
    val items: List<Item>,
    val title: String
) : Parcelable