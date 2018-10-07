package com.example.android.quakereport.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EarthquakeResponse {

    @SerializedName("features")
    @Expose
    val features: List<Features>? = null
}