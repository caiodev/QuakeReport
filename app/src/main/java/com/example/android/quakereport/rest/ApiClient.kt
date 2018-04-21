package com.example.android.quakereport.rest

import com.example.android.quakereport.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        return retrofit as Retrofit
    }
}