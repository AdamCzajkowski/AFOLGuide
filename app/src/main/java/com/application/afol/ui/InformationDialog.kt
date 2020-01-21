package com.application.afol.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.application.afol.BuildConfig
import com.application.afol.R
import kotlinx.android.synthetic.main.information_dialog.view.*

class InformationDialog(
    context: Context,
    themeResId: Int
) : AlertDialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        with(LayoutInflater.from(context).inflate(R.layout.information_dialog, null)) {
            versionTitleValue.text = BuildConfig.VERSION_NAME
            setView(this)
        }
        super.onCreate(savedInstanceState)
    }
}
