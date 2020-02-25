package com.application.afol.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.application.afol.R

enum class Label(@StringRes title: Int, @DrawableRes icon: Int, @ColorRes color: Int){
    ASSEMBLED(R.string.label_assembled, R.drawable.ic_assembled, R.color.yellow),
    UNASSAMBLED(R.string.label_unassembled, R.drawable.ic_unassmbled, R.color.blue),
    OWNED(R.string.label_owned, R.drawable.ic_owned, R.color.green),
    DESIRE(R.string.label_desire, R.drawable.ic_desire, R.color.red),
    INCOMPLETE(R.string.label_incomplete, R.drawable.ic_uncomplete, R.color.purple),
    COMPLETE(R.string.label_complete, R.drawable.ic_complete, R.color.orange )
}
