package com.example.android.quakereport

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.earth_quake_item_view_layout.view.*
import java.text.DecimalFormat
import java.util.*

/**
 * Created by unknown on 02/01/18
 */

class EarthquakeAdapter(context: Context, words: ArrayList<Earthquake>) :
        ArrayAdapter<Earthquake>(context, 0, words) {

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

        // Obtenha a cor de fundo apropriada, baseada na magnitude do terremoto atual
        val magnitudeCircle = magnitudeTextView.background as GradientDrawable

        // Busque o fundo do TextView, que é um GradientDrawable
        val magnitudeColor = getMagnitudeColor(currentEarthquakeValue.magnitude)

        // Configure a cor no círculo de magnitude
        magnitudeCircle.setColor(magnitudeColor)

        if (currentEarthquakeValue.nearOf != "") {

            nearOfTextView.text = context.getString(R.string.earth_quake_adapter_class_near_of_field_place_holder,
                    currentEarthquakeValue.nearOf, context.getString(R.string.of))
            println(nearOfTextView.text)

        } else {
            nearOfTextView.text = context.getString(R.string.earthquake_without_nearby_information_message)
            println(nearOfTextView.text)
        }

        placeTextView.text = currentEarthquakeValue.location
        dateTextView?.text = currentEarthquakeValue.date
        timeTextView?.text = currentEarthquakeValue.time

        return listItemView
    }

    private fun getMagnitudeColor(magnitude: Double?): Int {

        val magnitudeColorResourceId: Int
        val magnitudeFloor = Math.floor(magnitude!!.toDouble()).toInt()

        when (magnitudeFloor) {

            0 -> magnitudeColorResourceId = R.color.magnitude1
            1 -> magnitudeColorResourceId = R.color.magnitude1
            2 -> magnitudeColorResourceId = R.color.magnitude2
            3 -> magnitudeColorResourceId = R.color.magnitude3
            4 -> magnitudeColorResourceId = R.color.magnitude4
            5 -> magnitudeColorResourceId = R.color.magnitude5
            6 -> magnitudeColorResourceId = R.color.magnitude6
            7 -> magnitudeColorResourceId = R.color.magnitude7
            8 -> magnitudeColorResourceId = R.color.magnitude8
            9 -> magnitudeColorResourceId = R.color.magnitude9
            else -> magnitudeColorResourceId = R.color.magnitude10plus
        }

        return ContextCompat.getColor(context, magnitudeColorResourceId)
    }
}