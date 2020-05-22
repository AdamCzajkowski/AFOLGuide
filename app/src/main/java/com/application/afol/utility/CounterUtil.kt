package com.application.afol.utility

import android.content.Context
import com.application.afol.R

private const val SINGLE_ELEMENT = 1

fun setBrickSetCounterString(amountOfSets: Int, amountOfBricks: Int, context: Context) =
    StringBuilder().apply {
        append(amountOfSets)
        append(String.space)
        append(context.getText(if (amountOfSets == SINGLE_ELEMENT) R.string.counter_single_set else R.string.counter_multiple_sets ))
        append(String.space)
        append(amountOfBricks)
        append(String.space)
        append(context.getText(if (amountOfBricks == SINGLE_ELEMENT) R.string.counter_single_brick else R.string.counter_multiple_bricks ))
    }.toString()
