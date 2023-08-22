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
    val id: Int? = null,
    @SerializedName("set_num")
    val set_num: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("year")
    val year: Int? = null,
    @SerializedName("num_parts")
    val num_parts: Int? = null,
    @SerializedName("set_img_url")
    val set_img_url: String? = null,
    @SerializedName("set_url")
    val set_url: String? = null,
    var isInFavorite: Boolean = false
) : Parcelable