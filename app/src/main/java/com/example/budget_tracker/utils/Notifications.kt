package com.example.budget_tracker.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty


fun errorNotification(context: Context, message: String) {
    return Toasty.error(context, message, Toast.LENGTH_SHORT, true).show()
}

fun successNotification(context: Context, message: String) {
    return Toasty.success(context, message, Toast.LENGTH_SHORT, true).show()
}

fun infoNotification(context: Context, message: String) {
    return Toasty.info(context, message, Toast.LENGTH_SHORT, true).show()
}

fun warningNotification(context: Context, message: String) {
    return Toasty.warning(context, message, Toast.LENGTH_SHORT, true).show()
}
