package com.example.android.quakereport.utils

class Constants {

    companion object {
        const val dateMask = "MMM dd, yyyy"
        const val timeMask = "HH:mm"
        const val fullUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10"
        const val baseUrl = "https://earthquake.usgs.gov/fdsnws/event/1/"
        const val queryUrl = "query"
    }
}