package com.application.afol.extension

import androidx.room.TypeConverter
import com.application.afol.models.Label

class EnumTypeConverter {
    @TypeConverter
    fun toListOfLabel(value: String?): MutableList<Label>? {
        val listOfNames = value?.split(",")
        val listOfEnums = mutableListOf<Label>()

        listOfNames?.forEach {
            when (it) {
                Label.COMPLETE.name -> listOfEnums.add(Label.COMPLETE)
                Label.ASSEMBLED.name -> listOfEnums.add(Label.ASSEMBLED)
                Label.DESIRE.name -> listOfEnums.add(Label.DESIRE)
                Label.INCOMPLETE.name -> listOfEnums.add(Label.INCOMPLETE)
                Label.OWNED.name -> listOfEnums.add(Label.OWNED)
                Label.UNASSAMBLED.name -> listOfEnums.add(Label.UNASSAMBLED)
            }
        }

        return listOfEnums
    }

    @TypeConverter
    fun toString(listOfLabels: MutableList<Label>?): String? =
        listOfLabels?.joinToString(",") { it.name }
}