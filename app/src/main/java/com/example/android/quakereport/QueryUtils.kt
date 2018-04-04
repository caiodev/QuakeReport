package com.example.android.quakereport

import android.text.TextUtils
import com.example.android.quakereport.utils.Constants
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by unknown on 07/01/18
 */
class QueryUtils

internal constructor() {

    /**
     * Returns new URL object from the given string URL.
     */
    private fun createUrl(stringUrl: String?): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Timber.e(e, "Error with creating URL")
        }

        return url
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    @Throws(IOException::class)
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse
        }

        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            } else {
                Timber.e("Error with creating URL %s", urlConnection.responseCode)
            }
        } catch (e: IOException) {
            Timber.e(e, "Problem retrieving the earthquake JSON results")
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
            if (inputStream != null) {
                inputStream.close()
            }
        }
        return jsonResponse
    }

    /**
     * Convert the [InputStream] into a String which contains the
     * whole JSON response from the server.
     */
    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line: String? = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }

    /**
     * Query the USGS dataset and return an [Earthquake] object to represent a single earthquake.
     */
    fun fetchEarthquakeData(requestUrl: String?): List<Earthquake>? {

        // Create URL object
        val url = createUrl(requestUrl)

        // Perform HTTP request to the URL and receive a JSON response back
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Timber.e(e, "Error closing input stream")
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        // Return the {@link Event}
        return extractEarthquakes(jsonResponse)
    }

    private fun extractEarthquakes(earthquakeJson: String?): List<Earthquake>? {

        Timber.d("JSONResponse: %s", earthquakeJson)

        // Se a String do JSON é vazia ou nula, então retorna agora.
        if (TextUtils.isEmpty(earthquakeJson)) {
            return null
        }

        val earthquakes = ArrayList<Earthquake>()

        try {
            val rootJSON = JSONObject(earthquakeJson)
            val featuresJSONArray = rootJSON.getJSONArray("features")

            for (i in 0 until featuresJSONArray.length()) {

                val properties = featuresJSONArray.getJSONObject(i).getJSONObject("properties")

                //Split the string that comes from mock and pass it to the constructor
                if (properties.getString("place").contains("of")) {

                    val parts = properties.getString("place").split(" of ".toRegex()).dropLastWhile {
                        it.isEmpty()
                    }.toTypedArray()

                    println(parts[0])
                    println(parts[1])

                    earthquakes.add(Earthquake(properties.getDouble("mag"), parts[0], parts[1],
                            convertToDateOrTime(properties.getLong("time"), Constants.dateMask),
                            convertToDateOrTime(properties.getLong("time"), Constants.timeMask),
                            properties.getString("url")))

                } else {
                    earthquakes.add(Earthquake(properties.getDouble("mag"),
                            properties.getString("place"), convertToDateOrTime(properties.getLong("time"),
                            Constants.dateMask),
                            convertToDateOrTime(properties.getLong("time"), Constants.timeMask),
                            properties.getString("url")))
                }
            }

        } catch (e: JSONException) {
            Timber.tag("QueryUtils").e("Problem parsing the earthquake JSON results: %s",
                    e.printStackTrace())
        }

        return earthquakes
    }

    private fun convertToDateOrTime(timeInSeconds: Long, outputMask: String): String {
        val dateObject = Date(timeInSeconds)
        val dateFormatter = SimpleDateFormat(outputMask, Locale.getDefault())
        return dateFormatter.format(dateObject)
    }
}