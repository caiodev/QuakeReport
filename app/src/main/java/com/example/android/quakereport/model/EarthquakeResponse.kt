package com.example.android.quakereport.model

class EarthquakeResponse {

    val features: List<Features>? = null

    inner class Features {
        val properties: Properties? = null
    }

    inner class Properties {
        val mag: String? = null
        val place: String? = null
        val time: String? = null
        val url: String? = null
    }
}