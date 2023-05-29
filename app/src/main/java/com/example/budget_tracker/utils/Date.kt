package com.example.budget_tracker.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun convertDate(createdAt: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()
    val date = dateFormat.parse(createdAt)

    // Add 2 hours to the date
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.HOUR_OF_DAY, 2)
    val updatedDate = calendar.time

    val currentTimeMillis = System.currentTimeMillis()
    val dateMillis = updatedDate.time

    return DateUtils.getRelativeTimeSpanString(dateMillis, currentTimeMillis, DateUtils.MINUTE_IN_MILLIS).toString()
}