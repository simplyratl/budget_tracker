package com.example.budget_tracker.api.models.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    val price: Double,
    val name: String
) : Parcelable {
    // Class implementation
}