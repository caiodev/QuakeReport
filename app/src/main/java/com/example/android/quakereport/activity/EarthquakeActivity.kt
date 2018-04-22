package com.example.android.quakereport.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.example.android.quakereport.R
import com.example.android.quakereport.adapter.RecyclerViewAdapter
import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.EarthquakeResponse
import com.example.android.quakereport.rest.EarthquakeService
import com.example.android.quakereport.utils.ApiUtils
import com.example.android.quakereport.utils.ClickListener
import com.example.android.quakereport.utils.ProcessingUtils
import com.example.android.quakereport.utils.RecyclerItemTouchListener
import kotlinx.android.synthetic.main.earthquake_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EarthquakeActivity : AppCompatActivity() {

    private var processingUtils: ProcessingUtils? = null
    private var earthquakeService: EarthquakeService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        showProgressBar()

        processingUtils = ProcessingUtils()
        earthquakeService = ApiUtils().getEarthquakeService()

        loadResponseData()
    }

    private fun loadResponseData() {

        val earthquakeArrayList = ArrayList<Earthquake>()
        val earthquakeAdapter = RecyclerViewAdapter(applicationContext, earthquakeArrayList)
        mainEarthquakeRecyclerView.setHasFixedSize(true)
        mainEarthquakeRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mainEarthquakeRecyclerView.itemAnimator = DefaultItemAnimator()
        mainEarthquakeRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext, LinearLayoutManager.VERTICAL))
        mainEarthquakeRecyclerView.adapter = earthquakeAdapter

        val queryMapParameters = HashMap<String, String>()
        queryMapParameters["limit"] = "10"
        queryMapParameters["minmag"] = "6"
        queryMapParameters["orderby"] = "time"
        queryMapParameters["eventtype"] = "earthquake"
        queryMapParameters["format"] = "geojson"

        earthquakeService?.getAnswers(queryMapParameters)?.enqueue(object : Callback<EarthquakeResponse> {
            override fun onResponse(call: Call<EarthquakeResponse>, response: Response<EarthquakeResponse>) {

                if (response.isSuccessful) {
                    for (features: EarthquakeResponse.Features in response.body()?.features!!) {
                        processingUtils?.extractEarthquakes(features, earthquakeArrayList)
                    }

                    earthquakeAdapter.notifyDataSetChanged()

                    //OnItemClickListener
                    mainEarthquakeRecyclerView.addOnItemTouchListener(RecyclerItemTouchListener(applicationContext,
                            mainEarthquakeRecyclerView, object : ClickListener {

                        override fun onClick(view: View, position: Int) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(earthquakeArrayList[position].url)))
                        }

                        override fun onLongClick(view: View, position: Int) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    }))

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