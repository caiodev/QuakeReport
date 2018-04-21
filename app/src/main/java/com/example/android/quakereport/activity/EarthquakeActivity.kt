package com.example.android.quakereport.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import com.example.android.quakereport.R
import com.example.android.quakereport.adapter.EarthquakeAdapter
import com.example.android.quakereport.model.EarthquakeResponse
import com.example.android.quakereport.rest.EarthquakeService
import com.example.android.quakereport.utils.ApiUtils
import com.example.android.quakereport.utils.QueryUtils
import kotlinx.android.synthetic.main.earthquake_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthquakeActivity : AppCompatActivity() {

    private var queryUtils: QueryUtils? = null
    private var earthQuakeAdapter: EarthquakeAdapter? = null
    private var earthquakeService: EarthquakeService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        hideProgressBar()

        queryUtils = QueryUtils()

        earthquakeService = ApiUtils().getEarthquakeService()
        loadResponseData()

        earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, ArrayList())
        mainEarthquakeListView.adapter = earthQuakeAdapter

        //OnItemClickListener
        mainEarthquakeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            // Find the current earthquake that was clicked
            // Convert the String URL into an URI Object (To pass it on the Intent constructor)
            // Create a new Intent to visualize the earthquake and send the Intent to launch a new Activity
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(earthQuakeAdapter!!.getItem(position).url)))
        }
    }

    private fun loadResponseData() {

        showProgressBar()

        val queryMapParameters = HashMap<String, String>()
        queryMapParameters["limit"] = "1"
        queryMapParameters["minmag"] = "6"
        queryMapParameters["orderby"] = "time"
        queryMapParameters["eventtype"] = "earthquake"
        queryMapParameters["format"] = "geojson"

        earthquakeService?.getAnswers(queryMapParameters)?.enqueue(object : Callback<EarthquakeResponse> {
            override fun onResponse(call: Call<EarthquakeResponse>, response: Response<EarthquakeResponse>) {

                if (response.isSuccessful) {

                    for (features: EarthquakeResponse.Features in response.body()?.features!!) {
                        QueryUtils().extractEarthquakes(features, applicationContext, earthQuakeAdapter)
                    }

                    hideProgressBar()

                } else {
                    hideProgressBar()
                    val responseCode = response.code()
                    println("Status code: $responseCode")
                }
            }

            override fun onFailure(call: Call<EarthquakeResponse>, t: Throwable) {
                hideProgressBar()
                Log.d("MainActivity", "error loading from API")
            }
        })
    }

    private fun showProgressBar() {
        progressBar.show()
    }

    private fun hideProgressBar() {
        progressBar.hide()
    }
}