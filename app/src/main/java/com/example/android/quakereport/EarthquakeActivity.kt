package com.example.android.quakereport

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import kotlinx.android.synthetic.main.earthquake_activity.*


class EarthquakeActivity : AppCompatActivity() {

    //private val logTag = EarthquakeActivity::class.java.simpleName
    private var earthQuakes: ArrayList<Earthquake>? = null
    private var queryUtilsObject: QueryUtils? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        earthQuakes = ArrayList()

        // Cria uma lista falsa de earthquakes
        queryUtilsObject = QueryUtils()
        earthQuakes = queryUtilsObject!!.extractEarthquakes()

        val earthQuakeAdapter = EarthquakeAdapter(applicationContext!!, earthQuakes!!)

        mainEarthquakeListView.adapter = earthQuakeAdapter

        mainEarthquakeListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            // Achar o terremoto atual que foi clicado
            // Converte o URL String em um objeto URI (para passar no construtor de Intent)
            val earthquakeUri = Uri.parse(earthQuakeAdapter.getItem(position).url)

            // Cria um novo intent para visualizar a URI do earthquake
            // Envia o intent para lançar uma nova activity
            startActivity(Intent(Intent.ACTION_VIEW, earthquakeUri))
        }
    }
}