package com.example.android.quakereport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    private val logTag = EarthquakeActivity::class.java.simpleName
    private var earthQuakes: ArrayList<Earthquake>? = null
    private var queryUtilsObject: QueryUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        earthQuakes = ArrayList()

        // Cria uma lista falsa de earthquakes.
        queryUtilsObject = QueryUtils()
        earthQuakes = queryUtilsObject!!.extractEarthquakes()

        val earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, earthQuakes!!)

        mainEarthquakeListView.adapter = earthQuakeAdapter
    }
}