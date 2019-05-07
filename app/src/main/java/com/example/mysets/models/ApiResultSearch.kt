package com.example.mysets.models

import com.google.gson.annotations.SerializedName

data class ApiResultSearch(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: MutableList<LegoSet>
)
