package com.application.afol.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.application.afol.R
import com.application.afol.models.Label
import com.application.afol.models.LegoSet
import kotlinx.android.synthetic.main.dialog_select_label.*

class LabelsSelectorDialog(
    context: Context,
    themeResId: Int,
    val legoSet: LegoSet
) : AlertDialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        with(LayoutInflater.from(context).inflate(R.layout.dialog_select_label, null)) {
            showExistingLabels(legoSet.listOfLabels)
            setView(this)
        }
        super.onCreate(savedInstanceState)
    }

    private fun showExistingLabels(listOfLabels: MutableList<Label>?) {
        val helpfulListOfLabels = listOfLabels
        listOfLabels?.forEach {
            helpfulListOfLabels?.find { label -> label == Label.ASSEMBLED }
               /* label_row_1_left.background =
                context.getDrawable(R.drawable.label_background_selector_dialog_left_enable)
                label_row_1_left.background = R.drawable.label_background_selector_dialog_left_disable
*/        }
    }
}
}
