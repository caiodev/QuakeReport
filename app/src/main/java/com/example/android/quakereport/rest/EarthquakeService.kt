package com.example.android.quakereport.rest

import com.example.android.quakereport.model.EarthquakeResponse
import com.example.android.quakereport.utils.Constants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface EarthquakeService {

    @GET(Constants.queryUrl)
        fun getAnswers(@QueryMap format: Map<String, String>): Call<EarthquakeResponse>
}