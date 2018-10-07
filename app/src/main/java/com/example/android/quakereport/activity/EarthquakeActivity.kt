package com.example.android.quakereport.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.adapter.EarthquakeAdapter
import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.EarthquakeResponse
import com.example.android.quakereport.model.Features
import com.example.android.quakereport.rest.EarthquakeService
import com.example.android.quakereport.utils.*
import kotlinx.android.synthetic.main.earthquake_activity.*
import kotlinx.android.synthetic.main.earthquake_intensity_selector_layout.view.*
import kotlinx.android.synthetic.main.filters_dialog.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class EarthquakeActivity : AppCompatActivity() {

    private var processingUtils: ProcessingUtils? = null
    private var earthquakeService: EarthquakeService? = null
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        showProgressBar()

        if (NetworkState.checkNetworkAvailability(applicationContext)) {
            processingUtils = ProcessingUtils()
            earthquakeService = ApiUtils().getEarthquakeService()

            loadResponseData()

        } else {
            hideProgressBar()
            noInternetConnectionMessage.visibility = View.VISIBLE
        }
    }

    private fun loadResponseData() {

        val earthquakeArrayList = ArrayList<Earthquake>()
        val earthquakeAdapter = EarthquakeAdapter(applicationContext, earthquakeArrayList)
        mainEarthquakeRecyclerView.setHasFixedSize(true)
        mainEarthquakeRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        mainEarthquakeRecyclerView.itemAnimator = DefaultItemAnimator()
        mainEarthquakeRecyclerView.addItemDecoration(DividerItemDecoration(applicationContext,
                DividerItemDecoration.VERTICAL))
        mainEarthquakeRecyclerView.adapter = earthquakeAdapter

        val queryMapParameters = HashMap<String, String>()
        queryMapParameters["limit"] = "10"
        queryMapParameters["minmagnitude"] = "5"
        queryMapParameters["orderby"] = "time"
        queryMapParameters["eventtype"] = "earthquake"
        queryMapParameters["format"] = "geojson"

        earthquakeService?.getAnswers(queryMapParameters)?.enqueue(object : Callback<EarthquakeResponse> {
            override fun onResponse(call: Call<EarthquakeResponse>, response: Response<EarthquakeResponse>) {

                if (response.isSuccessful) {

                    for (features: Features in response.body()?.features!!) {
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == R.id.action_settings) {

            //Open a material dialog with the options
            materialDialogCreator(R.layout.filters_dialog)
            filterDialogLogic()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun materialDialogCreator(layout: Int) {

        val nullParent: ViewGroup? = null

        val dialog = AlertDialog.Builder(this)
        view = layoutInflater.inflate(layout, nullParent)
        dialog.setView(view).create().show()
    }

    private fun filterDialogLogic() {

        view.magnitudeFilters?.setOnClickListener {
            materialDialogCreator(R.layout.earthquake_intensity_selector_layout)
            earthquakeIntensityPickerDialog(view)
        }

        view.timeFilters?.setOnClickListener {

        }
    }

    private fun earthquakeIntensityPickerDialog(view: View) {

        view.ascCategoryRadioButton.setOnCheckedChangeListener { _: CompoundButton, isVisible: Boolean ->

            setRangeBarVisibility(isVisible)
        }

        view.descCategoryRadioButton.setOnCheckedChangeListener { _: CompoundButton, isVisible: Boolean ->

            setRangeBarVisibility(isVisible)
        }

        view.specificValueCategoryRadioButton.setOnCheckedChangeListener { _, isVisible: Boolean ->

            setRangeBarVisibility(isVisible)
        }

        view.rangeCategoryRadioButton.setOnCheckedChangeListener { _, isVisible: Boolean ->
            setRangeBarVisibility(isVisible)
        }
    }

    private fun toastMaker(message: String, length: Int) {
        Toast.makeText(applicationContext, message, length).show()
    }

    private fun showProgressBar() {
        progressBar.show()
    }

    private fun hideProgressBar() {
        progressBar.hide()
    }

    private fun setRangeBarVisibility(isVisible: Boolean) {
        if (isVisible) {
            view.intensityRangeBar.visibility = View.VISIBLE
        }
    }
}