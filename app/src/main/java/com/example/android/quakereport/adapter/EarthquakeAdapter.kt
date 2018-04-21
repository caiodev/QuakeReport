package com.example.android.quakereport.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.android.quakereport.R
import com.example.android.quakereport.model.Earthquake
import kotlinx.android.synthetic.main.earth_quake_item_view_layout.view.*
import java.text.DecimalFormat

/**
 * Created by unknown on 02/01/18
 */
class EarthquakeAdapter(context: Context, earthquakes: List<Earthquake>) :
        ArrayAdapter<Earthquake>(context, 0, earthquakes) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var listItemView = convertView

        if (listItemView == null) {
            listItemView = LayoutInflater.from(context).inflate(
                    R.layout.earth_quake_item_view_layout, parent, false)
        }

        val currentEarthquakeValue = getItem(position)

        val magnitudeTextView = listItemView!!.magnitude
        val nearOfTextView = listItemView.nearOf
        val placeTextView = listItemView.location
        val dateTextView = listItemView.date
        val timeTextView = listItemView.time

        magnitudeTextView.text = DecimalFormat("0.0").format(currentEarthquakeValue.magnitude)

        // magnitudeCircle obtains the right background color based on the current earthquake magnitude
        val magnitudeCircle = magnitudeTextView.background as GradientDrawable

        // magnitudeColor obtains the background color TextView, que é um GradientDrawable
        val magnitudeColor = getMagnitudeColor(currentEarthquakeValue.magnitude)

        // Here, the magnitude circle color is set
        magnitudeCircle.setColor(magnitudeColor)

        if (currentEarthquakeValue.nearOf != "") {

            nearOfTextView.text = context.getString(R.string.earth_quake_adapter_class_near_of_field_place_holder,
                    currentEarthquakeValue.nearOf, context.getString(R.string.of))

        } else {
            nearOfTextView.text = context.getString(R.string.earthquake_without_nearby_information_message)
        }

        placeTextView.text = currentEarthquakeValue.place
        dateTextView.text = currentEarthquakeValue.date
        timeTextView.text = currentEarthquakeValue.time

        return listItemView
    }

    private fun getMagnitudeColor(magnitude: Double?): Int {

        val magnitudeFloor = Math.floor(magnitude!!.toDouble()).toInt()

        return when (magnitudeFloor) {

            0 -> returnColor(context, R.color.magnitude1)
            1 -> returnColor(context, R.color.magnitude1)
            2 -> returnColor(context, R.color.magnitude2)
            3 -> returnColor(context, R.color.magnitude3)
            4 -> returnColor(context, R.color.magnitude4)
            5 -> returnColor(context, R.color.magnitude5)
            6 -> returnColor(context, R.color.magnitude6)
            7 -> returnColor(context, R.color.magnitude7)
            8 -> returnColor(context, R.color.magnitude8)
            9 -> returnColor(context, R.color.magnitude9)
            else -> returnColor(context, R.color.magnitude10plus)
        }
    }

    private fun returnColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}