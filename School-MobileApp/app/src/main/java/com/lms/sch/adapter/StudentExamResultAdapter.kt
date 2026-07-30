package com.lms.sch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardClassTestReportBinding
import com.lms.sch.response.GetExamResponse

class StudentExamResultAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<GetExamResponse.Row>
): RecyclerView.Adapter<StudentExamResultAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var binding: CardClassTestReportBinding = CardClassTestReportBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity).inflate(R.layout.card_class_test_report,parent,false)
        )
    }

    override fun getItemCount(): Int{
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        /*if (list[position].subject != null){
            holder.binding.subName.text = list[position].subject!!.name
        }
        else{
            holder.binding.subName.text = "--/--"
        }
        if (list[position].totalMarks != null && list[position].scoredMarks != null){
            holder.binding.marks.text = list[position].scoredMarks!! + "/" + list[position].totalMarks
        }
        else{
            holder.binding.marks.text = "--/--"
        }
        if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
            holder.binding!!.attach.text = "+${list[position].attachment!!}"
        }
        else{
            holder.binding!!.attach.text = "--None--"
        }
        holder.binding!!.attach.setOnClickListener {
            if (list[position].attachment != null && list[position].attachment!!.isNotEmpty()){
                if (list[position].attachment!![0].endsWith(".pdf")){
                    val bundle = Bundle()
                    bundle.putString(Constants.IntentKeys.KEY, list[position].attachment!![0])
                    BaseUtils.startActivity(mActivity, PdfViewerActivity(),bundle,false)
                }
                else if (list[position].attachment!![0].endsWith(".png") || list[position].attachment!![0].endsWith(".jpeg") || list[position].attachment!![0].endsWith(".jpg")){
                    val doc = ArrayList<String>()
                    for (items in list[position].attachment!!){
                        if (items.endsWith(".jpg") || items.endsWith(".jpeg") || items.endsWith(".png")){
                            doc.add(items)
                        }
                    }
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.IntentKeys.KEY,doc)
                    BaseUtils.startActivity(mActivity, ImageViewActivity(),bundle,false)
                }
                else {
                    if (list[position].attachment != null && list[position].attachment!![0].isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.setData(Uri.parse(list[position].attachment!![0]))
                        mActivity.startActivity(intent)
                    }
                    else {
                        UiUtils.showSnack("We are unable to fetch the Image",holder.binding!!.root,false)
                    }
                }
            }
        }*/

    }

}