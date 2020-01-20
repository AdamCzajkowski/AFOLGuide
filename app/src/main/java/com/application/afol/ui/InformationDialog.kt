package com.application.afol.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.application.afol.R

class InformationDialog(
    context: Context,
    themeResId: Int
) : AlertDialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        with(LayoutInflater.from(context).inflate(R.layout.information_dialog, null)) {
            setView(this)
        }
        super.onCreate(savedInstanceState)
    }
}
