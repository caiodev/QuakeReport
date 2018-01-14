package com.example.android.quakereport

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.earth_quake_item_view_layout.view.*
import java.util.*

/**
 * Created by unknown on 02/01/18
 */

class EarthquakeAdapter(context: Context, words: ArrayList<Earthquake>)
    : ArrayAdapter<Earthquake>(context, 0, words) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.earth_quake_item_view_layout, parent, false)
        }

        val currentEarthquakeValue = getItem(position)

        val magnitudeTextView = listItemView!!.magnitude
        magnitudeTextView.text = currentEarthquakeValue.magnitude.toString()

        val placeTextView = listItemView.place
        placeTextView.text = currentEarthquakeValue.place

        val dateTextView = listItemView.date
        dateTextView?.text = currentEarthquakeValue.date.toString()

        return listItemView
    }
}