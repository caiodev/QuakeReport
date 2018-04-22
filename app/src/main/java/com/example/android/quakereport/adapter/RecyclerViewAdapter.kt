package com.example.android.quakereport.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.android.quakereport.R
import com.example.android.quakereport.model.Earthquake
import kotlinx.android.synthetic.main.earth_quake_item_view_layout.view.*
import java.text.DecimalFormat

class RecyclerViewAdapter(private val context: Context, private val earthquakes: ArrayList<Earthquake>?) :
        RecyclerView.Adapter<RecyclerViewAdapter.EarthquakeViewHolder>() {

    private var viewHolder: EarthquakeViewHolder? = null

    inner class EarthquakeViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        val magnitudeTextView: TextView? = itemView!!.magnitude
        val nearOfTextView: TextView? = itemView!!.nearOf
        val placeTextView: TextView? = itemView!!.place
        val dateTextView: TextView? = itemView!!.date
        val timeTextView: TextView? = itemView!!.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EarthquakeViewHolder {
        return EarthquakeViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.earth_quake_item_view_layout, parent, false))
    }

    override fun onBindViewHolder(holder: EarthquakeViewHolder, position: Int) {

        viewHolder = holder
        val earthquake = earthquakes?.get(viewHolder!!.adapterPosition)

        if (earthquake != null && viewHolder != null) {

            viewHolder!!.magnitudeTextView?.text = DecimalFormat("0.0").format(earthquake.magnitude)

            // magnitudeCircle obtains the right background color based on the current earthquake magnitude
            val magnitudeCircle = viewHolder!!.magnitudeTextView?.background as GradientDrawable

            // magnitudeColor obtains the background color TextView, que é um GradientDrawable
            val magnitudeColor = getMagnitudeColor(earthquake.magnitude)

            // Here, the magnitude circle color is set
            magnitudeCircle.setColor(magnitudeColor)

            when {
                earthquake.nearOf != "" -> viewHolder!!.nearOfTextView?.text = context.getString(R.string.earth_quake_adapter_class_near_of_field_place_holder,
                        earthquake.nearOf, context.getString(R.string.of))
                else -> viewHolder!!.nearOfTextView?.text = context.getString(R.string.earthquake_without_nearby_information_message)
            }

            viewHolder!!.placeTextView?.text = earthquake.place
            viewHolder!!.dateTextView?.text = earthquake.date
            viewHolder!!.timeTextView?.text = earthquake.time
        }
    }

    override fun getItemCount(): Int {
        return earthquakes!!.size
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