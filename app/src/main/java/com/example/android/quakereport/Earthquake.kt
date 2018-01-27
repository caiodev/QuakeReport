package com.example.android.quakereport

/**
 * Created by unknown on 02/01/18
 */

data class Earthquake(val magnitude: Double?, val nearOf: String, val location: String?,
                      val date: String?, val time: String?) {

    constructor(magnitude: Double?, location: String?, date: String?, time: String?) :
            this(magnitude, "", location, date, time)
}