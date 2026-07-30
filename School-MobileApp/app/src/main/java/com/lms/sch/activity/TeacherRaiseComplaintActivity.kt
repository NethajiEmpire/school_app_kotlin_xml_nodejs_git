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
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.lms.sch.R
import com.lms.sch.adapter.GetComplaintsAdapter
import com.lms.sch.adapter.SpinnerAdapter
import com.lms.sch.databinding.ActivityComplaintsBinding
import com.lms.sch.databinding.ActivityTeacherRaiseComplaintBinding
import com.lms.sch.databinding.FilterHomeworkBinding
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.network.ApiConnection
import com.lms.sch.response.GetComplaintDropDownResponse
import com.lms.sch.response.GetComplaintResponse
import com.lms.sch.response.GetComplaintStatResponse
import com.lms.sch.response.GetRoleResponse
import com.lms.sch.response.GetStudentExamProgressResponse
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.DialogUtils
import com.lms.sch.utils.UiUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class TeacherRaiseComplaintActivity : BaseActivity() {
    lateinit var binding: ActivityTeacherRaiseComplaintBinding
    var role = ArrayList<GetRoleResponse.Result>()
    var complaintStatRes : GetComplaintStatResponse.Result? = null
    var allCompRes = ArrayList<GetComplaintResponse.Result.Rows>()
    var complaintAttach = ArrayList<String>()
    var roleId = ""
    var roleStsCount = ""
    var complaintStatus = ""
    var search = ""
    var count = 0
    var complaintType = ""
    var toWhom = ""
    var status = ""
    override lateinit var sharedHelper: SharedHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherRaiseComplaintBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        sharedHelper = SharedHelper(this)
        binding.backarrow.setOnClickListener {
            onBackPressed()
        }
        complaintAttach.clear()
        dropDownComplaintType()

        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                search = p0.toString()
                getAllComplaints(search)

            }
        })

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
                            role = it.result!!
                            val filteredRoleList = ArrayList<GetRoleResponse.Result>()
                            val userRole = sharedHelper.role
                            val roles = when (userRole) {
                                "STUDENT" -> arrayListOf("STUDENT")
                                "TEACHER" -> arrayListOf("TEACHER", "STUDENT", "PARENT")
                                "PARENT" -> arrayListOf("PARENT", "STUDENT")
                                else -> arrayListOf("")
                            }

                            for (roleItem in it.result!!) {
                                val roleName = roleItem.name?.uppercase()
                                if (roleName in roles) {
                                    if (roleName == userRole){
                                        roleItem.name = "My Complaints"
                                    }
                                    else {
                                       roleItem.name?.toCamelCase()
                                    }
                                    filteredRoleList.add(roleItem)
                                }
                            }
                            role = filteredRoleList
                            if (role.isNotEmpty()) {
                                roleId = role[0]._id!!
                                roleStsCount = role[0]._id!!
                                getComplaintStats()
                            }
                            if (userRole != "STUDENT") {
                                initAdapter(layoutInflater, binding.root)
                                binding.tabLayout.visibility = View.VISIBLE
                            } else {
                                binding.tabLayout.visibility = View.GONE
                            }
                            getAllComplaints(search)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }
        }

        binding.filter.setOnClickListener {
            val inflater = LayoutInflater.from(this)
            val bind : FilterHomeworkBinding = FilterHomeworkBinding.inflate(inflater)
            bind.today.visibility = View.GONE
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
            bind.pending.text = "Unsolved"
            bind.completed.text = "Solved"
            if (status == "unsolved"){
                UiUtils.textviewImgDrawable(bind.pending,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                bind.today.visibility = View.GONE
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            else if (status == "solved"){
                UiUtils.textviewImgDrawable(bind.completed,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.all,null,"start")
                bind.today.visibility = View.GONE
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
            }
            else {
                UiUtils.textviewImgDrawable(bind.all,R.drawable.hugeicons_tick,"start")
                UiUtils.textviewImgDrawable(bind.pending,null,"start")
                bind.today.visibility = View.GONE
                UiUtils.textviewImgDrawable(bind.completed,null,"start")
            }
            bind.all.setOnClickListener {
                status = ""
                complaintStatus = ""
                getAllComplaints(search)
                popupWindow.dismiss()
            }
            bind.pending.setOnClickListener {
                status = "unsolved"
                complaintStatus = "unsolved"
                getAllComplaints(search)
                popupWindow.dismiss()
            }
            bind.completed.setOnClickListener {
                status = "solved"
                complaintStatus = "solved"
                getAllComplaints(search)
                popupWindow.dismiss()
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

        binding.totalCmpTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = ""
            search = ""
            status = ""
            binding.search.setText("")
            getComplaintStats()
            //roleStsCount = ""
            getAllComplaints(search)
        }
        binding.unsCompTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = "unsolved"
            search = ""
            status = "unsolved"
            binding.search.setText("")
            getComplaintStats()
            getAllComplaints(search)

        }
        binding.solveTab.setOnClickListener {
            UiUtils.linearLayoutBgDrawable(binding.solveTab, R.drawable.border_line_curve_8dp_light_blues)
            UiUtils.linearLayoutBgDrawable(binding.totalCmpTab, R.drawable.border_line_curve_8dp_grey)
            UiUtils.linearLayoutBgDrawable(binding.unsCompTab, R.drawable.border_line_curve_8dp_grey)
            complaintStatus = "solved"
            search = ""
            status = "solved"
            binding.search.setText("")
            getComplaintStats()
            getAllComplaints(search)
        }
        binding.totalCmpTab.performClick()

        binding.raiseTab.setOnClickListener {
            binding.page1.visibility = View.GONE
            binding.page2.visibility = View.VISIBLE
            binding.topHeader.text = "Raise Complaint"
        }
        binding.attach.setOnClickListener {
            openDocList()
        }
        complaintAttach.clear()

        val userRole = sharedHelper.role
        val spin = ArrayList<String>()
        spin.add("Select..")
        when (userRole) {
            "TEACHER" -> {
                spin.add("Parent")
                spin.add("Admin")
                toWhom = "PARENT"
                binding.spinner1.setSelection(spin.indexOf("Parent"))
            }
            "STUDENT" -> {
                spin.add("Teacher")
                spin.add("Admin")
            }
            "PARENT" -> {
                spin.add("Teacher")
                spin.add("Admin")
            }
            else -> {
                spin.add("Admin")
                spin.add("Student")
                spin.add("Teacher")
                spin.add("Parent")
            }
        }
        val adapter = SpinnerAdapter(this, spin)
        binding.spinner1.adapter = adapter
        binding.spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val clickedValue = parent.getItemAtPosition(position) as String
                toWhom = when (clickedValue) {
                    "Admin" -> "ADMIN"
                    "Student" -> "STUDENT"
                    "Teacher" -> "TEACHER"
                    "Parent" -> "PARENT"
                    else -> ""
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                toWhom = ""
            }
        }

        binding.submit.setOnClickListener {
            val complaintsTitle = binding.title.text.toString()
            val description = binding.desc.text.toString()

            if (complaintsTitle.isEmpty()) {
                UiUtils.showSnack("Please enter a complaint title", binding.root, false)
                return@setOnClickListener
            }
            if (description.isEmpty()) {
                UiUtils.showSnack("Please enter a complaint description", binding.root, false)
                return@setOnClickListener
            }
            if (complaintType.isEmpty()) {
                UiUtils.showSnack("Please select a complaint type", binding.root, false)
                return@setOnClickListener
            }
            if (complaintAttach.isEmpty()) {
                UiUtils.showSnack("Please attach complaint document", binding.root, false)
                return@setOnClickListener
            }
            if (toWhom.isEmpty()) {
                UiUtils.showSnack("Please select a recipient for the complaint", binding.root, false)
                return@setOnClickListener
            }

            if (complaintAttach != null) {
                ApiConnection.getInstance().complaintRaise(this, complaintsTitle, description, complaintType, toWhom, complaintAttach).observe(this) {
                    it.let {
                        DialogUtils.dismissLoader()
                        it.success.let { success ->
                            if (success) {
                                UiUtils.showSnack(it.msg, binding.root, true)
                                binding.title.setText("")
                                binding.desc.setText("")
                                complaintAttach.clear()
                                search = ""
                                binding.search.setText("")
                                binding.page1.visibility = View.VISIBLE
                                binding.page2.visibility = View.GONE
                                binding.topHeader.text = "Complaints"
                                getAllComplaints(search)
                            } else {
                                UiUtils.showSnack(it.msg, binding.root, false)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        if (binding.page2.visibility == View.VISIBLE){
            binding.page1.visibility = View.VISIBLE
            binding.page2.visibility = View.GONE
            binding.topHeader.text = "Complaints"
            search = ""
            binding.search.setText("")
            getAllComplaints(search)
            binding.title.setText("")
            binding.desc.setText("")
            complaintAttach.clear()
        }
        else {
            super.onBackPressed()
        }
    }

    private fun dropDownComplaintType() {
        DialogUtils.showLoader(this)
        ApiConnection.getInstance().getComplaintDropDown(this).observe(this) {
            it?.let {
                DialogUtils.dismissLoader()
                it.success.let { success ->
                    if (success) {
                        if (it.result != null && it.result!!.isNotEmpty()) {
                            val compTypes = ArrayList<String>()
                            compTypes.add("Select ....")
                            for (item in it.result!!) {
                                compTypes.add(item.label!!)
                            }
                            val adapter = SpinnerAdapter(this, compTypes)
                            binding.spinner.adapter = adapter
                            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                    if (position != 0) {
                                        complaintType = it.result!![position - 1].value!!
                                        // complaintType = dropDownId
                                    } else {
                                        complaintType = ""
                                    }
                                }
                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                                }
                            } else {
                            UiUtils.showSnack(it.msg, binding.root, false)
                        }
                    } else {
                        UiUtils.showSnack(it.msg, binding.root, false)
                    }
                }
            }?: run {
                DialogUtils.dismissLoader()
                UiUtils.showSnack("Something went wrong. Please try again later.", binding.root, false)
            }
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
        ApiConnection.getInstance().getAllComplaints(this, search, complaintStatus, roleId ,"").observe(this) {
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
                                        if (sharedHelper.role == "TEACHER" || sharedHelper.role == "PARENT") {
                                            if (allCompRes[pos].status == "unsolved") {
                                                getDialogComplaint(pos)
                                            }
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
                            // UiUtils.showSnack(it.msg, binding.root, false)
                            binding.noData.root.visibility = View.VISIBLE
                            binding.complaintsRecyclerView.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    fun getDialogComplaint(pos: Int){
        binding.dialogComplaint.root.visibility = View.VISIBLE
        binding.dialogComplaint.close.setOnClickListener {
            binding.dialogComplaint.root.visibility = View.GONE
        }
        if (allCompRes[pos].createdBy != null && allCompRes[pos].createdBy!!.firstName != null && allCompRes[pos].createdBy!!.lastName != null) {
            binding!!.dialogComplaint.name.text = "${allCompRes[pos].createdBy!!.firstName ?: ""} ${allCompRes[pos].createdBy!!.lastName ?: ""}"
        } else {
            binding.dialogComplaint.cmpName.text = "--/--"
        }
        if (allCompRes[pos].createdBy != null && allCompRes[pos].createdBy!!.email !=null) {
            binding!!.dialogComplaint.txtEmail1.text = allCompRes[pos].createdBy!!.email
        } else {
            binding.dialogComplaint.txtEmail1.text = "--/--"
        }
        if (allCompRes[pos].title != null && allCompRes[pos].title!!.isNotEmpty()) {
            binding.dialogComplaint.cmpName.text = allCompRes[pos].title
        } else {
            binding.dialogComplaint.cmpName.text = "--/--"
        }
        if (allCompRes[pos].description != null && allCompRes[pos].description!!.isNotEmpty()) {
            binding.dialogComplaint.des.text = allCompRes[pos].description
        } else {
            binding.dialogComplaint.des.text = "--/--"
        }

        when (allCompRes[pos].status) {
            "solved" -> {
                binding!!.dialogComplaint.status.text = "Solved"
                UiUtils.textviewCustomDrawable(binding!!.dialogComplaint.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding!!.dialogComplaint.status, "#E5F8ED", null)
                UiUtils.textViewTextColor(binding!!.dialogComplaint.status, "#32B138", null)
            }

            "unsolved" -> {
                binding!!.dialogComplaint.status.text = "Unsolved"
                UiUtils.textviewCustomDrawable(binding!!.dialogComplaint.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding!!.dialogComplaint.status, "#FFEBEB", null)
                UiUtils.textViewTextColor(binding!!.dialogComplaint.status, "#EA5455", null)
            }
            else -> {
                binding.dialogComplaint.status.text = "--/--"
                UiUtils.textviewCustomDrawable(binding.dialogComplaint.status, R.drawable.border_curve_16dp)
                UiUtils.textViewBgTint(binding.dialogComplaint.status, "#ECECEC", null)
                UiUtils.textViewTextColor(binding.dialogComplaint.status, "#888888", null)
            }
        }

        binding.dialogComplaint.attach.setOnClickListener {
            openDocList()
        }
        binding.dialogComplaint.solve.setOnClickListener {
            val complaintId = allCompRes[pos]._id
            if (complaintAttach.isEmpty()) {
                UiUtils.showSnack("Please upload a document", binding.root, false)
                return@setOnClickListener
            }
            if (binding.dialogComplaint.solution.text.toString().isEmpty()) {
                UiUtils.showSnack("Please enter a solution", binding.root, false)
                return@setOnClickListener
            }
            else {
                if (complaintId != null){
                    ApiConnection.getInstance().complaintSolve(this,complaintId,complaintAttach,binding.dialogComplaint.solution.text.toString()).observe(this){
                        it.let {
                            DialogUtils.dismissLoader()
                            it.success.let { success->
                                if (success){
                                    UiUtils.showSnack(it.msg,binding.root,true)
                                    complaintAttach.clear()
                                    binding.dialogComplaint.root.visibility = View.GONE
                                    search = ""
                                    binding.search.setText("")
                                    getAllComplaints(search)
                                } else {
                                    UiUtils.showSnack(it.msg, binding.root, false)
                                }
                            }
                        }
                    }
                }
                else {
                    UiUtils.showSnack("Something went wrong.",binding.root,false)
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
            if (intent.resolveActivity(packageManager) != null) {
                startActivityForResult(intent, 12)
            } else {
                UiUtils.showSnack("No supported app found to open files", binding.root, false)
            }
           // startActivityForResult(intent, 12)
        } else {
            // Validation: Notify and request permission
            UiUtils.showSnack("Storage permission is required to access documents", binding.root, false)
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
                        complaintAttach.clear()
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

            binding.tabLayout.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
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