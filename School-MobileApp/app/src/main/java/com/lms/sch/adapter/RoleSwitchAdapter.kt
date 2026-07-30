import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lms.sch.R
import com.lms.sch.activity.BaseActivity
import com.lms.sch.databinding.CardStudentHwCompletedBinding
import com.lms.sch.databinding.CardStudentHwPendingBinding
import com.lms.sch.databinding.CardSwitchBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.response.GetStudentAssignmentResponse
import com.lms.sch.response.ParentProfileResponse
import com.lms.sch.utils.UiUtils

class RoleSwitchAdapter(
    val mActivity: BaseActivity,
    val list: ArrayList<ParentProfileResponse.Result.UserProfile.Students>,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<RoleSwitchAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = CardSwitchBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(mActivity) .inflate(R.layout.card_switch, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (list[position].user_id != null){
            holder.binding.name.text = list[position].user_id!!.firstName +" "+ list[position].user_id!!.lastName
            holder.binding.rollNo.text = list[position].user_id!!.lead_id
            if (list[position].current_user){
                mActivity.sharedHelper.childId = list[position].user_id!!._id!!
                holder.binding.tick.visibility = View.VISIBLE
            }
            else {
                holder.binding.tick.visibility = View.GONE
            }
            holder.binding.root.setOnClickListener {
                if (!list[position].current_user){
                    onClickListener.onClickItem(position)
                }
            }
        }
    }
}
