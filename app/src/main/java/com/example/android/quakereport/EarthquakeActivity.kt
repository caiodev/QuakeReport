package com.example.android.quakereport

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    private var queryUtils: QueryUtils? = null
    private var earthQuakeAdapter: EarthquakeAdapter? = null
    private val requestUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        hideProgressBar()

        queryUtils = QueryUtils()

        earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, ArrayList())
        mainEarthquakeListView.adapter = earthQuakeAdapter

        val earthquakeAsyncTask = EarthquakeAsyncTask()
        earthquakeAsyncTask.execute(requestUrl)

        //OnItemClickListener
        mainEarthquakeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            // Find the current earthquake that was clicked
            // Convert the String URL into an URI Object (To pass it on the Intent constructor)
            // Create a new Intent to visualize the earthquake and send the Intent to launch a new Activity
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(earthQuakeAdapter!!.getItem(position).url)))
        }
    }

    private fun showProgressBar() {
        progressBar.show()
    }

    private fun hideProgressBar() {
        progressBar.hide()
    }

    inner class EarthquakeAsyncTask : AsyncTask<String, Void, List<Earthquake>>() {

        override fun doInBackground(vararg params: String?): List<Earthquake>? {

            showProgressBar()

            return if (params.isEmpty() || params[0] == null) {
                null
            } else queryUtils?.fetchEarthquakeData(params[0])
        }

        override fun onPostExecute(result: List<Earthquake>) {
            super.onPostExecute(result)

            EarthquakeAdapter(applicationContext, result)

            // It clears old Earthquake data from the adapter
            earthQuakeAdapter?.clear()

            // If there is a valid Earthquake list, add it to the adapter dataset
            // This will activate the ListView update.
            if (!result.isEmpty()) {
                earthQuakeAdapter?.addAll(result)
            }

            hideProgressBar()
        }
    }
}