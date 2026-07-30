package com.lms.sch.customviews

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.lms.sch.R
import com.lms.sch.activity.SubjectWiseProgressActivity
import com.lms.sch.response.GetStudentClassTestProgress
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils

class CustomMarkerView2(
    private val context: Context,
    private val list: ArrayList<GetStudentClassTestProgress.Result>
) : MarkerView(context, R.layout.card_graph_values2) {

    private val scheduledOn: TextView = findViewById(R.id.scheduledOn)
    private val subject: TextView = findViewById(R.id.subject)
    private val percentage: TextView = findViewById(R.id.percentage)
    private val markerCard: View = findViewById(R.id.mark) // bind to root view

    private var currentIndex: Int = -1

    init {
        markerCard.setOnClickListener {
            if (currentIndex in list.indices) {
                val item = list[currentIndex]
                val intent = Intent(context, SubjectWiseProgressActivity::class.java)
                intent.putExtra("subjectName", item.subject ?: "")
                context.startActivity(intent)
            }
        }
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            currentIndex = (e.x / 2).toInt() // spacing correction

            if (currentIndex in list.indices) {
                val item = list[currentIndex]

                val schedule = item.scheduledOn?.let {
                    BaseUtils.getFormattedDate(it, Constants.ApiKeys.TIME_INPUT_FORMAT1, Constants.ApiKeys.DATE_FORMAT)
                } ?: "--/--"

                val dueDate = item.dueDate?.let {
                    BaseUtils.getFormattedDate(it, Constants.ApiKeys.TIME_INPUT_FORMAT1, Constants.ApiKeys.DATE_FORMAT)
                } ?: "--/--"

                scheduledOn.text = "$schedule - $dueDate"
                subject.text = "${item.subject ?: "--/--"} CT conducted : 04"

                val percent = item.percentage?.toFloatOrNull() ?: 0f
                percentage.text = "${percent.toInt()}%"
            }
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
