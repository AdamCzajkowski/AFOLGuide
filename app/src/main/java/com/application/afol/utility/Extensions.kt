package com.application.afol.utility

import android.view.View
import com.google.android.material.snackbar.Snackbar

val doNothing = Unit

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) =
    Snackbar.make(
        this,
        message,
        duration
    ).show()

fun String.dropLastTwoChars() = dropLast(2)