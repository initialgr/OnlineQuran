package com.app.muslim.models


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("surahs")
    val surahs: List<Surah>
)