package com.example.android.quakereport

import com.example.android.quakereport.utils.ProcessingUtils
import org.junit.Assert
import org.junit.Test

/**
 * Created by unknown on 22/01/18
 */

class ProcessingUtilsTest {

    val queryUtils = ProcessingUtils()

    @Test
    fun extractEarthquakes() {
    }

    @Test
    fun convertToDate() {
        Assert.assertEquals("Jan 22, 2018", queryUtils.convertToDate(1516667843))
    }
}