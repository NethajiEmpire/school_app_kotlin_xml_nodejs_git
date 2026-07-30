package com.lms.sch.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.GetComplaintsAdapter
import com.lms.sch.databinding.ActivityComplaintsBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetComplaintResponse
import com.lms.sch.response.GetComplaintSingleViewResponse
import com.lms.sch.response.GetComplaintStatResponse
import com.lms.sch.response.GetRoleResponse
import com.lms.sch.session.Constants
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class ComplaintsActivity : BaseActivity() {
    lateinit var binding: ActivityComplaintsBinding
    var allCompRes = ArrayList<GetComplaintResponse.Result.Rows>()
    var complaintStatRes : GetComplaintStatResponse.Result? = null
    var role = ArrayList<GetRoleResponse.Result>()
    var singleIdResult: GetComplaintSingleViewResponse.Result? = null
    var complaintAttach = ArrayList<String>()
    var search = ""
    var roleId = ""
    var complaintStatus = ""
    var roleStsCount = ""
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = p0.toString()
                getAllComplaints(search)

            }
        })
        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)

            bind.today.text = "Unsolved"
            bind.pending.text = "Solved"
            bind.completed.visibility = View.GONE
            val popupView : View = bind.root
            val widthInDp = 120
            val density = resources.displayMetrics.density
            val widthInPx = (widthInDp * density).toInt()

            val popupWindow = PopupWindow(popupView,widthInPx,ViewGroup.LayoutParams.WRAP_CONTENT,true)
            popupWindow.isOutsideTouchable = true
            popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popupWindow.elevation = 8f
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.setElevation(8f)
            }
            if (complaintStatus == ""){
                UiUtils.textviewImgDrawable(bind.today,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (complaintStatus == "unsolved"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (complaintStatus == "solved"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
//            else if (status == "approved"){
//                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
//                UiUtils.textviewImgDrawable(bind.all,null,"start")
//                UiUtils.textviewImgDrawable(bind.today,null,"start")
//                UiUtils.textviewImgDrawable(bind.pending,null,"start")
//            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                UiUtils.textviewImgDrawable(bind.today,null,"start")
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            bind.all.setOnClickListener {
                complaintStatus = ""
                search = ""
                binding.search.setText("")
                getComplaintStats()
                getAllComplaints(search)
                popupWindow.dismiss()
            }
            bind.today.setOnClickListener {
                complaintStatus = "unsolved"
                search = ""
                binding.search.setText("")
                getComplaintStats()
                getAllComplaints(search)
            }
            bind.pending.setOnClickListener {
                complaintStatus = "solved"
                search = ""
                binding.search.setText("")
                getComplaintStats()
                getAllComplaints(search)
            }

            val anchorView = binding.filter
            val location = IntArray(2)
            anchorView.getLocationOnScreen(location)

            val endGapDp = 8
            val topGapDp = 8
            val endGapPx = (endGapDp * density).toInt()
            val topGapPx = (topGapDp * density).toInt()
            val xPos = location[0] + anchorView.width - widthInPx - endGapPx
            val yPos = location[1] + anchorView.height + topGapPx

            popupWindow.showAtLocation(
                anchorView,
                Gravity.NO_GRAVITY,
                xPos,
                yPos
            )
        }

        fun String.toCamelCase(): String {
            return lowercase().replaceFirstChar { it.uppercase() }
        }

        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getRole(this).observe(this) {
            it.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                           // roleStsCount = role!!.toString()
                            val filteredRoleList = ArrayList<GetRoleResponse.Result>()
                            for (roleItem in it.result!!) {
                                val roleName = roleItem.name?.uppercase()
                                if (roleName == "STUDENT" || roleName == "TEACHER" || roleName == "PARENT" || roleName == "STAFF") {
                                    roleItem.name = roleItem.name?.toCamelCase()
                                    filteredRoleList.add(roleItem)
                                }
                            }
                            role = filteredRoleList
                          //  roleStsCount = role[0]._id!!
                            if (role.isNotEmpty()) {
                                roleId = role[0]._id!!
                                roleStsCount = role[0]._id!!
                                getComplaintStats()
                            }
                            initAdapter(layoutInflater, binding.root)
                         //   getComplaintStats()
                            getAllComplaints(search)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }

        binding.totalCmpTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_grey)
            // UiUtils.linearLayoutBgDrawable(binding.pendingCmpTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = ""
            search = ""
            binding.search.setText("")
            getComplaintStats()
            getAllComplaints(search)
        }
        binding.unsCompTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_grey)
            // UiUtils.linearLayoutBgDrawable(binding.pendingCmpTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = "unsolved"
            search = ""
            binding.search.setText("")
            getComplaintStats()
            getAllComplaints(search)

        }
        binding.solveTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_grey)
            // UiUtils.linearLayoutBgDrawable(binding.pendingCmpTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = "solved"
            search = ""
            binding.search.setText("")
            getComplaintStats()
            getAllComplaints(search)
        }
        binding.totalCmpTab.performClick()

    }

    override fun onBackPressed() {
        if (binding.page2.visibility == View.VISIBLE){
            binding.page1.visibility = View.VISIBLE
            binding.page2.visibility = View.GONE
            search = ""
            binding.search.setText("")
            getAllComplaints(search)
            binding.topHeader.text = "Complaints"
        } else {
            super.onBackPressed()
        }
    }

    private fun getComplaintStats(){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getComplaintStats(this, roleStsCount).observe(this){
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if(success){
                        if (it.result != null){
                            complaintStatRes = it.result!!
                            Log.d("kljhgf", complaintStatRes.toString())

                            if (complaintStatRes!!.total != null){
                                binding.cmpNum.text = it.result!!.total.toString()
                            }
                            else{
                                binding.cmpNum.text = "--/--"
                            }
                            if (complaintStatRes!!.unsolved != null){
                                binding.uncmpNum.text = it.result!!.unsolved.toString()
                            }
                            else{
                                binding.uncmpNum.text = "--/--"
                            }
                            if (complaintStatRes!!.solved != null){
                                binding.solNum.text = it.result!!.solved.toString()
                            }
                            else{
                                binding.solNum.text = "--/--"
                            }
                        }
                        else{
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    }
                    else{
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }
    }

    private fun getAllComplaints(search: String) {
        if (search.isEmpty()) {
            DialogUtils.showLoader(this)
        }
        ApiConnection.getInstance().getAllComplaints(this, search, complaintStatus, roleId,"" ).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null) {
                            if (it.result != null && it.result!!.rows!!.isNotEmpty()) {
                                allCompRes= it.result!!.rows!!
                                binding.noData.root.visibility = View.GONE
                                binding.complaintsRecyclerView.visibility = View.VISIBLE
                                val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
                                val adapter = GetComplaintsAdapter(this,  it.result!!.rows!!,object : OnClickListener {
                                    override fun onClickItem(pos: Int) {
                                        if (allCompRes[pos].status == "unsolved") {
                                            binding.page1.visibility = View.GONE
                                            binding.page2.visibility = View.VISIBLE
                                            binding.topHeader.text = "Solve Complaint"
                                            loadComplaint(pos)
                                        }
                                    }
                                })
                                binding.complaintsRecyclerView.layoutManager = layoutManager
                                binding.complaintsRecyclerView.adapter = adapter
                            } else{
                                binding.noData.root.visibility = View.VISIBLE
                                binding.complaintsRecyclerView.visibility = View.GONE
                            }
                        } else {
                            binding.noData.root.visibility = View.VISIBLE
                            binding.complaintsRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    fun loadComplaint(pos: Int){
        if (allCompRes[pos].createdBy != null && allCompRes[pos].createdBy!!.firstName != null && allCompRes[pos].createdBy!!.lastName != null) {
            binding.name.text = "${allCompRes[pos].createdBy!!.firstName ?: ""} ${allCompRes[pos].createdBy!!.lastName ?: ""}"
        } else {
            binding.cmpName.text = "--/--"
        }
        if (allCompRes[pos].createdBy != null && allCompRes[pos].createdBy!!.email !=null) {
            binding.txtEmail1.text = allCompRes[pos].createdBy!!.email
        } else {
            binding.txtEmail1.text = "--/--"
        }
        if (allCompRes[pos].title != null && allCompRes[pos].title!!.isNotEmpty()) {
            binding.cmpName.text = allCompRes[pos].title
        } else {
            binding.cmpName.text = "--/--"
        }
        if (allCompRes[pos].description != null && allCompRes[pos].description!!.isNotEmpty()) {
            binding.des.setContent(allCompRes[pos].description)
        } else {
            binding.des.setContent("--/--")
        }

        when (allCompRes[pos].status) {
            "solved" -> {
                binding.status.text = "Solved"
                UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding.status, "#E5F8ED", null)
                UiUtils.textViewTextColor(binding.status, "#32B138", null)
            }

            "unsolved" -> {
                binding.status.text = "Unsolved"
                UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding.status, "#FFEBEB", null)
                UiUtils.textViewTextColor(binding.status, "#EA5455", null)
            }
            else -> {
                binding.status.text = allCompRes[pos].status
                UiUtils.textviewCustomDrawable(binding.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding.status, "#ECECEC", null)
                UiUtils.textViewTextColor(binding.status, "#888888", null)
            }
        }
        binding.attach.setOnClickListener {
            openDocList()
        }

        binding.solve.setOnClickListener {
            val complaintId = allCompRes[pos]._id
            if (complaintAttach.isNullOrEmpty()) {
                UiUtils.showSnack("Please upload attachment", binding.root, false)
                return@setOnClickListener
            }
            else if (binding.solution.text.isEmpty()){
                UiUtils.showSnack("Please enter solution", binding.root, false)
                return@setOnClickListener
            }
            else {
                if (complaintId != null && complaintId.isNotEmpty()){
                    DialogUtils.showLoader(this)
                    ApiConnection.getInstance().complaintSolve(this, complaintId, complaintAttach,binding.solution.text.toString()).observe(this) {
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success ->
                                if (success) {
                                    UiUtils.showSnack(it.msg, binding.root, true)
                                    complaintAttach.clear()
                                    binding.page2.visibility = View.GONE
                                    binding.page1.visibility = View.VISIBLE
                                    binding.title.text = "Complaints"
                                    search = ""
                                    binding.search.setText("")
                                    getAllComplaints(search)
                                }
                                else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Something went wrong", binding.root, false)
                }
            }
        }
    }

    fun openDocList() {
        if (BaseUtils.isPermissionsEnabled(this, Constants.IntentKeys.STORAGE)) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            intent.putExtra(
                Intent.EXTRA_MIME_TYPES, arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg",
                    "application/pdf",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
                    "application/msword"
                )
            )
            startActivityForResult(intent, 12)
        } else {
            BaseUtils.permissionsEnableRequest(this, Constants.IntentKeys.STORAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 12 && resultCode == RESULT_OK) {
            if (data != null){
                if (data.clipData != null){
                    var docs = data.clipData
                    for (items in 0 until  docs!!.itemCount){
                        count++
                        val uri = docs.getItemAt(items).uri
                        var filePart: MultipartBody.Part? = null
                        if (uri != null) {
                            val documentFile = DocumentFile.fromSingleUri(this, uri)
                            val fileInputStream = this.contentResolver.openInputStream(uri)
                            val mimeType = this.contentResolver.getType(uri)
                            val buffer = fileInputStream?.readBytes()
                            fileInputStream?.close()
                            if (buffer != null && mimeType != null) {
                                val fileSize = buffer.size
                                val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                                val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                                filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                                val size = BaseUtils.convertBytes(fileSize.toLong())
                                Log.d("dv1", "" + documentFile?.name)
                                Log.d("dv0", "" + uri.path)
                                Log.d("dv3", "" + size)
                                Log.d("dv4", "" + mimeType)
                                if (fileSizeInMB <= 5){
                                    val json = JSONObject()
                                    json.put("name",documentFile?.name)
                                    json.put("size",size)
                                    json.put("type",mimeType)
//                                    urlName.add(json)
                                    upload(filePart)
                                } else {
                                    UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
                                }
                            }
                        }
                    }
                } else {
                    val uri = data?.data
                    var filePart: MultipartBody.Part? = null
                    count++
                    if (uri != null) {
                        val documentFile = DocumentFile.fromSingleUri(this, uri)
                        val fileInputStream = this.contentResolver.openInputStream(uri)
                        val mimeType = this.contentResolver.getType(uri)
                        val buffer = fileInputStream?.readBytes()
                        fileInputStream?.close()
                        if (buffer != null && mimeType != null) {
                            val fileSize = buffer.size
                            val fileSizeInMB = fileSize / (1024.0 * 1024.0)
                            val fileBody = RequestBody.create(mimeType.toMediaTypeOrNull(), buffer)
                            filePart = MultipartBody.Part.createFormData("file", documentFile?.name, fileBody)
                            val size = BaseUtils.convertBytes(fileSize.toLong())
                            Log.d("dv1", "" + documentFile?.name)
                            Log.d("dv0", "" + uri.path)
                            Log.d("dv3", "" + size)
                            Log.d("dv4", "" + mimeType)
                            if (fileSizeInMB <= 5){
                                val json = JSONObject()
                                json.put("name",documentFile?.name)
                                json.put("size",size)
                                json.put("type",mimeType)
                                binding.attach.text = documentFile?.name
                                upload(filePart)
                            } else {
                                UiUtils.showSnack("File size exceeds 5 MB", binding.root,false)
                            }
                        }
                    }
                }
            }
        }
    }

    fun upload(filepart: MultipartBody.Part){
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().uploadFile(this, filepart).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success && it.result.isNotEmpty()) {
                        UiUtils.showSnack(it.msg, binding.root,true)
                        val url = it.result[0].location!!
                        complaintAttach.add(url)

                    }
                    else {
                        UiUtils.showSnack(it.msg, binding.root,false)
                    }
                }
            }
        }
    }

    private fun initAdapter(inflater: LayoutInflater, container: ViewGroup) {
        if (binding.tabLayout.tabCount == 0) {
            for (i in 0 until role.size) {

                binding.tabLayout.addTab(binding.tabLayout.newTab().setText(""))
                val tabView: View = inflater.inflate(R.layout.custom_tab, container, false)
                val tabText = tabView.findViewById<TextView>(R.id.tab)

                tabText.text = role[i].name
                UiUtils.textViewTextColor(tabText, null, R.color.colorPrimary)
                tabText.setTextAppearance(R.style.FontMedium)

                binding.tabLayout.getTabAt(i)?.customView = tabView
            }

            binding.tabLayout.tabGravity = TabLayout.GRAVITY_CENTER
            binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

            binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {

                    val linear0 = tab.customView ?: return
                    val txttab0 = linear0.findViewById<TextView>(R.id.tab)

                    UiUtils.textViewTextColor(txttab0, null, R.color.colorPrimary)
                    txttab0.setTextAppearance(R.style.FontMedium)
                    if (tab.position < role.size) {
                        roleId = role[tab.position]._id!!
                        roleStsCount = role[tab.position]._id!!
                        getAllComplaints(search)
                        getComplaintStats()
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {

                    val linear1 = tab.customView ?: return
                    val txttab1 = linear1.findViewById<TextView>(R.id.tab)

                    UiUtils.textViewTextColor(txttab1, null, R.color.black)
                    txttab1.setTextAppearance(R.style.FontMedium)
                }

                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        } else {
            binding.tabLayout.removeAllTabs()
            binding.tabLayout.clearOnTabSelectedListeners()
            initAdapter(inflater, container)
        }
    }

}