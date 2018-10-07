package com.example.android.quakereport.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Properties {

    @SerializedName("mag")
    @Expose
    val earthquakeMagnitude: String? = null

    @SerializedName("place")
    @Expose
    val earthquakePlace: String? = null

    @SerializedName("time")
    @Expose
    val earthquakeTime: String? = null

    @SerializedName("url")
    @Expose
    val earthquakeUrl: String? = null
}