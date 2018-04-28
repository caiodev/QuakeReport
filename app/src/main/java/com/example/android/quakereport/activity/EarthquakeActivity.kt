package com.example.android.quakereport.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.adapter.EarthquakeAdapter
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
import timber.log.Timber

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
        val earthquakeAdapter = EarthquakeAdapter(applicationContext, earthquakeArrayList)
        mainEarthquakeRecyclerView.setHasFixedSize(true)
        mainEarthquakeRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mainEarthquakeRecyclerView.itemAnimator = DefaultItemAnimator()
        mainEarthquakeRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext,
                LinearLayoutManager.VERTICAL))
        mainEarthquakeRecyclerView.adapter = earthquakeAdapter

        val queryMapParameters = HashMap<String, String>()
        queryMapParameters["limit"] = "10"
        queryMapParameters["minmag"] = "5"
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
                            Toast.makeText(applicationContext, earthquakeArrayList[position].place,
                                    Toast.LENGTH_LONG).show()
                        }
                    }))

                    hideProgressBar()

                } else {
                    hideProgressBar()
                    Timber.d("Response code: %s", response.code())
                }
            }

            override fun onFailure(call: Call<EarthquakeResponse>, t: Throwable) {
                hideProgressBar()
                Timber.d("Error loading from API: %s", t.message)
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