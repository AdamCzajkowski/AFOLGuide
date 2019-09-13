package com.application.afol.models


import com.google.gson.annotations.SerializedName

data class MOCResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: Any?,
    @SerializedName("previous")
    val previous: Any?,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("designer_name")
        val designerName: String,
        @SerializedName("designer_url")
        val designerUrl: String,
        @SerializedName("moc_img_url")
        val mocImgUrl: String,
        @SerializedName("moc_url")
        val mocUrl: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("num_parts")
        val numParts: Int,
        @SerializedName("set_num")
        val setNum: String,
        @SerializedName("year")
        val year: Int
    )
}