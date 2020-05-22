package com.application.afol.utility


import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

private const val TWO_CHARS = 2

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

val String.Companion.empty
    get() = ""

val String.Companion.space
    get() = " "

fun String.dropLastTwoChars() = dropLast(TWO_CHARS)

fun AlertDialog.showDialog() = apply {
    show()
    window?.apply {
        setGravity(Gravity.CENTER)
        setLayout(WRAP_CONTENT, WRAP_CONTENT)
    }
}

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun drawable(context: Context, @DrawableRes id: Int): Drawable? =
    AppCompatResources.getDrawable(context, id)

@ColorInt
fun Context.color(@ColorRes color: Int) =
    ContextCompat.getColor(this, color)
