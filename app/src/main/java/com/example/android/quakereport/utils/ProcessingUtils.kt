package com.example.android.quakereport.utils

import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.Features
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by unknown on 07/01/18
 */

class ProcessingUtils {

    fun extractEarthquakes(features: Features?,
                           earthquakeAdapterList: ArrayList<Earthquake>): ArrayList<Earthquake> {

        features?.properties?.let { nonNullableProperties ->

            if (nonNullableProperties.earthquakePlace != null &&
                    nonNullableProperties.earthquakeMagnitude != null &&
                    nonNullableProperties.earthquakeTime != null) {

                if (nonNullableProperties.earthquakePlace.contains("of")) {

                    val parts = nonNullableProperties.earthquakePlace.split(" of ".toRegex())
                            .dropLastWhile { it.isEmpty() }.toTypedArray()

                    earthquakeAdapterList.add(Earthquake(nonNullableProperties.earthquakeMagnitude.toDouble(),
                            parts[0], parts[1],
                            convertToDateOrTime(nonNullableProperties.earthquakeTime.toLong(), Constants.dateMask),
                            convertToDateOrTime(nonNullableProperties.earthquakeTime.toLong(), Constants.timeMask),
                            nonNullableProperties.earthquakeUrl ?: ""))

                } else {

                    earthquakeAdapterList.add(Earthquake(nonNullableProperties.earthquakeMagnitude.toDouble(),
                            nonNullableProperties.earthquakePlace,
                            convertToDateOrTime(nonNullableProperties.earthquakeTime.toLong(),
                                    Constants.dateMask),
                            convertToDateOrTime(nonNullableProperties.earthquakeTime.toLong(),
                                    Constants.timeMask),
                            nonNullableProperties.earthquakeUrl ?: ""))
                }
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