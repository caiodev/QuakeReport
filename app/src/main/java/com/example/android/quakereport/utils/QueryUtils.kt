package com.example.android.quakereport.utils

import android.content.Context
import com.example.android.quakereport.adapter.EarthquakeAdapter
import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.EarthquakeResponse
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by unknown on 07/01/18
 */
class QueryUtils {

    fun extractEarthquakes(earthquakes: EarthquakeResponse.Features?, context: Context,
                           earthquakeAdapter: EarthquakeAdapter?) {

        if (earthquakes != null) {

            val earthquakeAdapterList = ArrayList<Earthquake>()

            if (earthquakes.properties!!.place!!.contains("of")) {

                val parts = earthquakes.properties.place!!.split(" of ".toRegex()).dropLastWhile {
                    it.isEmpty()
                }.toTypedArray()

                earthquakeAdapterList.add(Earthquake(earthquakes.properties.mag!!.toDouble(), parts[0], parts[1],
                        convertToDateOrTime(earthquakes.properties.time!!.toLong(), Constants.dateMask),
                        convertToDateOrTime(earthquakes.properties.time.toLong(), Constants.timeMask),
                        earthquakes.properties.url))

            } else {
                earthquakeAdapterList.add(Earthquake(earthquakes.properties.mag!!.toDouble(), earthquakes.properties.place,
                        convertToDateOrTime(earthquakes.properties.time!!.toLong(), Constants.dateMask),
                        convertToDateOrTime(earthquakes.properties.time.toLong(), Constants.timeMask),
                        earthquakes.properties.url))
            }

            EarthquakeAdapter(context, earthquakeAdapterList)

            // It clears old Earthquake data from the adapter
            earthquakeAdapter?.clear()

            // If there is a valid Earthquake list, add it to the adapter dataset
            // This will activate the ListView update.
            if (!earthquakeAdapter!!.isEmpty) {
                earthquakeAdapter.addAll(earthquakeAdapterList)
            }
        }
    }

    private fun convertToDateOrTime(timeInSeconds: Long, outputMask: String): String {
        val dateObject = Date(timeInSeconds)
        val dateFormatter = SimpleDateFormat(outputMask, Locale.getDefault())
        return dateFormatter.format(dateObject)
    }
}