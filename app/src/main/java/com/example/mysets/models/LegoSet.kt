package com.example.mysets.models

import com.google.gson.annotations.SerializedName

data class LegoSet(
    @SerializedName("set_num")
    val set_num: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("year")
    val year: Int,
    @SerializedName("num_parts")
    val num_parts: Int,
    @SerializedName("set_img_url")
    val set_img_url: String,
    @SerializedName("set_url")
    val set_url: String
)