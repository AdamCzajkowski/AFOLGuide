package com.application.afol.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class LegoSet(
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @SerializedName("set_num")
    val set_num: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("year")
    val year: Int?,
    @SerializedName("num_parts")
    val num_parts: Int?,
    @SerializedName("set_img_url")
    val set_img_url: String?,
    @SerializedName("set_url")
    val set_url: String?,
    var isInFavorite: Boolean = false
) : Parcelable