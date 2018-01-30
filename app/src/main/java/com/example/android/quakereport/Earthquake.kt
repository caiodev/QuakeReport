package com.example.android.quakereport

/**
 * Created by unknown on 02/01/18
 */

data class Earthquake(val magnitude: Double?, val nearOf: String, val location: String?,
                      val date: String?, val time: String?, val url: String?) {

    constructor(magnitude: Double?, location: String?, date: String?, time: String?, url: String?) :
            this(magnitude, "", location, date, time, url)
}