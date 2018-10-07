package com.example.android.quakereport.model

/**
 * Created by unknown on 02/01/18
 */

data class Earthquake(val magnitude: Double, val nearOf: String, val place: String,
                      val date: String, val time: String, val url: String) {

    constructor(magnitude: Double, place: String, date: String, time: String, url: String) :
            this(magnitude, "", place, date, time, url)
}