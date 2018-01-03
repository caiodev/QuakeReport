package com.example.android.quakereport

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    private val logTag = EarthquakeActivity::class.java.simpleName
    private var earthQuakes: ArrayList<Earthquake>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        earthQuakes = ArrayList()

        // Create a fake list of earthquake locations
        earthQuakes!!.add(Earthquake(7.2, "San Andreas, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(7.0, "San Diego, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(6.8, "San Francisco, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(8.5, "Los Angeles, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(6.4, "Hollywood, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(7.5, "Mountain View, California", "30/10/2017"))
        earthQuakes!!.add(Earthquake(7.8, "Houston, Texas", "30/10/2017"))

        val earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, earthQuakes!!)

        mainEarthquakeListView.adapter = earthQuakeAdapter
    }
}