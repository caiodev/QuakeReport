package com.example.android.quakereport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    private var earthQuakes: ArrayList<Earthquake>? = null
    private var queryUtilsObject: QueryUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        earthQuakes = ArrayList()

        // A fake earthquake list is created
        queryUtilsObject = QueryUtils()
        earthQuakes = queryUtilsObject!!.extractEarthquakes()

        val earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, earthQuakes!!)

        mainEarthquakeListView.adapter = earthQuakeAdapter

        mainEarthquakeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            // Find the current earthquake that was clicked
            // Convert the String URL into an URI Object (To pass it on the Intent constructor)
            // Create a new Intent to visualize the earthquake URI
            // Send the Intent to launch a new Activity
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(earthQuakeAdapter.getItem(position).url)))
        }
    }
}