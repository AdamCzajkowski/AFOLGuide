package com.application.afol.utility


import android.content.Context
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.appcompat.app.AlertDialog
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

fun AlertDialog.showDialog() = apply {
    show()
    window?.setGravity(Gravity.CENTER)
    window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}
