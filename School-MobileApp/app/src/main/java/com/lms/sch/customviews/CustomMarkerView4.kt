package com.lms.sch.customviews

import android.content.Context
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.lms.sch.R
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.response.SubjectWiseClassExamProResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils

class CustomMarkerView4 (
    context: Context,
    var list: ArrayList<SubjectWiseClassExamProResponse.Result>
) : MarkerView(context, R.layout.card_graph_values2) {

//    private val scheduledOn: TextView = findViewById(R.id.scheduledOn)
    private val subject: TextView = findViewById(R.id.subject)
    private val percentage: TextView = findViewById(R.id.percentage)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val index = e.x.toInt()

            if (index in list.indices) {
                val item = list[index]
                if (item != null) {
                    if (item.subject != null) {
                        subject.text = "${item.subject} CT conducted : 04"
                    } else {
                        subject.text = "--/--"
                    }
                    if (item.percentage != null) {
                        val percent = item.percentage!!.toFloat() ?: 0f
                        percentage.text = "${percent.toInt()}%"
                    } else {
                        percentage.text = "--/--"
                    }
                }
            }
            super.refreshContent(e, highlight)
        }
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}