package com.example.android.quakereport

import org.junit.Assert
import org.junit.Test

/**
 * Created by unknown on 22/01/18
 */

class QueryUtilsTest {

    val queryUtils = QueryUtils()

    @Test
    fun extractEarthquakes() {
    }

    @Test
    fun convertToDate() {
        Assert.assertEquals("Jan 22, 2018", queryUtils.convertToDate(1516667843))
    }
}