package com.example.mysets.models

import com.google.gson.annotations.SerializedName

data class PartResult(
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
        @SerializedName("name")
        val name: String,
        @SerializedName("part_cat_id")
        val partCatId: Int,
        @SerializedName("part_img_url")
        val partImgUrl: String,
        @SerializedName("part_num")
        val partNum: String,
        @SerializedName("part_url")
        val partUrl: String,
        @SerializedName("print_of")
        val printOf: Any?
    )
}