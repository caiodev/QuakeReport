package com.example.android.quakereport.utils

import com.example.android.quakereport.rest.ApiClient
import com.example.android.quakereport.rest.EarthquakeService

class ApiUtils {

    fun getEarthquakeService(): EarthquakeService {
        return ApiClient().getClient().create(EarthquakeService::class.java)
    }
}