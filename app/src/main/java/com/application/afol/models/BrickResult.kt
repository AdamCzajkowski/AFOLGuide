package com.application.afol.models


import com.google.gson.annotations.SerializedName

data class BrickResult(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: MutableList<Result>
) {
    data class Result(
        @SerializedName("color")
        val color: Color,
        @SerializedName("part")
        val part: Part,
        @SerializedName("quantity")
        val quantity: Int
    ) {
        data class Color(
            @SerializedName("name")
            val name: String
        )

        data class Part(
            @SerializedName("name")
            val name: String,
            @SerializedName("part_img_url")
            val partImgUrl: String,
            @SerializedName("part_num")
            val partNum: String,
            @SerializedName("part_url")
            val partUrl: String,
            @SerializedName("external_ids")
            val externalIds: Bricklink?
        ) {
            data class Bricklink(
                @SerializedName("BrickLink")
                val bricklink: MutableList<String>?,
                @SerializedName("BrickOwl")
                val brickowl: MutableList<String>?
            )
        }
    }
}