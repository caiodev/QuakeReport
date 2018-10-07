package com.example.android.quakereport.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkState {

    fun checkNetworkAvailability(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnectedOrConnecting
    }
}