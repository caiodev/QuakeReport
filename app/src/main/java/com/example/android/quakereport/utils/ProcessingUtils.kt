package com.example.android.quakereport.utils

import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.EarthquakeResponse
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by unknown on 07/01/18
 */
class ProcessingUtils {

    fun extractEarthquakes(earthquakes: EarthquakeResponse.Features?,
                           earthquakeAdapterList: ArrayList<Earthquake>) : ArrayList<Earthquake> {

        if (earthquakes != null) {

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
        }

        return earthquakeAdapterList
    }

    private fun convertToDateOrTime(timeInSeconds: Long, outputMask: String): String {
        val dateObject = Date(timeInSeconds)
        val dateFormatter = SimpleDateFormat(outputMask, Locale.getDefault())
        return dateFormatter.format(dateObject)
    }
}