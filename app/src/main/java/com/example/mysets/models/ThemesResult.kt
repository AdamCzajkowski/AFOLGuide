package com.example.mysets.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class ThemesResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}

@Parcelize
class GroupedThemes(
    val name: String,
    val listOfID: MutableList<Int>
) : Parcelable