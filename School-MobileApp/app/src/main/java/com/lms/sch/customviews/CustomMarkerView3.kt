package com.lms.sch.customviews

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.lms.sch.R
import com.lms.sch.response.GetStudentExamProgressResponse

class CustomMarkerView3(
    context: Context,
    val resultList: ArrayList<GetStudentExamProgressResponse.Result>
) : MarkerView(context, R.layout.card_graph_values2) {

    private val scheduledOn: TextView = findViewById(R.id.scheduledOn)
    private val subject: TextView = findViewById(R.id.subject)
    private val percentage: TextView = findViewById(R.id.percentage)
    private val averageMarks: TextView = findViewById(R.id.averageMarks)

    init {
        subject.visibility = GONE
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val index = e.x.toInt()

            if (index in resultList.indices) {
                val result = resultList[index]
                val examType = result.examType?.name ?: "Exam"

                scheduledOn.text = "${examType} (${result.startsDate ?: "--/--"} - ${result.endsDate ?: "--/--"})"

                if (!result.subject.isNullOrEmpty()) {
                    subject.text = result.subject
                    subject.visibility = VISIBLE
                } else {
                    subject.visibility = GONE
                }

                averageMarks.text = "Marks: ${result.scoredMarks ?: "--"} / ${result.totalMarks ?: "--"}"

                val percent = result.percentage?.toFloatOrNull()?.toInt() ?: 0
                percentage.text = "$percent%"
            } else {
                scheduledOn.text = "--/--"
                subject.visibility = GONE
                averageMarks.text = "--/--"
                percentage.text = "--%"
            }
            super.refreshContent(e, highlight)
        }
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}
