package com.lms.sch.customviews

import android.content.Context
import android.widget.TextView
import com.lms.sch.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarkerView(context: Context) : MarkerView(context, R.layout.card_graph_values) {
    private val markerText: TextView = findViewById(R.id.markerText)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            // Update the markerText with the entry's value
            val value = e.y // Get the Y value of the entry
            markerText.text = "${value.toInt()}%" // Format the value (e.g., add % symbol)
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}