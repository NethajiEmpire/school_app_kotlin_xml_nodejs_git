package com.lms.sch.activity

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lms.sch.R
import com.lms.sch.databinding.ActivityDashBoardBinding
import com.lms.sch.utils.BaseUtils
import com.lms.sch.utils.UiUtils

class DashBoardActivity : BaseActivity() {
    lateinit var binding: ActivityDashBoardBinding
    var iscor = false
    var isBelow5 = false
    var navView: BottomNavigationView? = null
    var navController:NavController? = null
    var currentPage = 0
    private var lastBackPressTime = 0L
    private val doubleBackPressInterval = 2000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)
        val page = BaseUtils.nullCheckerStr(intent.getStringExtra("page"))
        val id = BaseUtils.nullCheckerStr(intent.getStringExtra("id"))
        isBelow5 = BaseUtils.nullCheckerBoolean(intent.getBooleanExtra("isBelow5",false))

        navView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_progress,
            R.id.navigation_fees,
            R.id.navigation_chat
        ))
        //       setupActionBarWithNavController(navController, appBarConfiguration)
        //  navView!!.setupWithNavController(navController!!)
        val role = sharedHelper.role
        updateMenuForRole(role)
        val navHostFragment = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment)
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.mobile_navigation)
       
        if (role == "ADMIN") {
            graph.setStartDestination(R.id.navigation_Home_admin)
        }
        else if (role == "STUDENT") {
            if (isBelow5){
                graph.setStartDestination(R.id.navigation_home_new)
            }
            else {
                graph.setStartDestination(R.id.navigation_home)
            }
        }
        else if (role == "TEACHER") {
            graph.setStartDestination(R.id.navigation_home_teacher)
        }
        else if (role == "PARENT") {
            graph.setStartDestination(R.id.navigation_home_parent)
        }
        else {
            graph.setStartDestination(R.id.navigation_home)
        }

        navHostFragment.navController.graph = graph
        NavigationUI.setupWithNavController(binding.navView, navController!!)

//        navView?.menu?.setGroupCheckable(0, false, true)
//        navView?.selectedItemId = -1

        navView!!.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    navController!!.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_home_new -> {
                    navController!!.navigate(R.id.navigation_home_new)
                    true
                }
                R.id.navigation_home_teacher -> {
                    navController!!.navigate(R.id.navigation_home_teacher)
                    true
                }
                R.id.navigation_Home_admin -> {
                    navController!!.navigate(R.id.navigation_Home_admin)
                    true
                }
                R.id.navigation_home_parent ->{
                    navController!!.navigate(R.id.navigation_home_parent)
                    true
                }
                R.id.navigation_Admin_User_Management -> {
                    navController!!.navigate(R.id.navigation_Admin_User_Management)
                    true
                }
                R.id.navigation_Admin_Finance -> {
                    navController!!.navigate(R.id.navigation_Admin_Finance)
                    true
                }
                R.id.navigation_progress -> {
                    navController!!.navigate(R.id.navigation_progress)
                    true
                }
                R.id.navigation_parent_child_progress -> {
                    navController!!.navigate(R.id.navigation_parent_child_progress)
                    true
                }
                R.id.navigation_my_class -> {
                    navController!!.navigate(R.id.navigation_my_class)
                    true
                }
                R.id.navigation_fees -> {
                    navController!!.navigate(R.id.navigation_fees)
                    true
                }
                R.id.navigation_my_subject -> {
                    navController!!.navigate(R.id.navigation_my_subject)
                    true
                }
                R.id.navigation_chat -> {
                    navController!!.navigate(R.id.navigation_chat)
                    true
                }
                else -> {
                    false
                }
            }
            true
        }
    }

    fun selectBottomNav(bnavid:Int){
        if(binding != null){
            when (bnavid) {
                0 -> {
                    binding.navView.menu.getItem(0).isChecked = true
                    binding.navView.menu.getItem(1).isChecked = false
                    binding.navView.menu.getItem(2).isChecked = false
                    binding.navView.menu.getItem(3).isChecked = false
                    binding.navView.menu.getItem(3).isCheckable = false
                    binding.nav1.visibility = View.VISIBLE
                    binding.nav2.visibility = View.INVISIBLE
                    binding.nav3.visibility = View.INVISIBLE
                    binding.nav4.visibility = View.INVISIBLE
                    binding.name1.text = "Home"
                    UiUtils.imageviewDrawable(binding.img1, R.drawable.home_selected)
                }
                1 -> {
                    binding.navView.menu.getItem(0).isChecked = false
                    binding.navView.menu.getItem(1).isChecked = true
                    binding.navView.menu.getItem(2).isChecked = false
                    binding.navView.menu.getItem(3).isChecked = false
                    binding.navView.menu.getItem(3).isCheckable = false
                    binding.nav1.visibility = View.INVISIBLE
                    binding.nav2.visibility = View.VISIBLE
                    binding.nav3.visibility = View.INVISIBLE
                    binding.nav4.visibility = View.INVISIBLE
                    when(sharedHelper.role) {
                       "STUDENT","PARENT"  ->{
                           binding.name2.text = "Progress"
                           UiUtils.imageviewDrawable(binding.img2, R.drawable.progress_selected)
                       }
                        "TEACHER" ->{
                            binding.name2.text = "Progress"
                            UiUtils.imageviewDrawable(binding.img2, R.drawable.my_class_selected)
                        }
                        "ADMIN" ->{
                            binding.name2.text = "Users"
                            UiUtils.imageviewDrawable(binding.img2, R.drawable.subject_selected)
                        }
                        else -> {
                            binding.name2.text = "Progress"
                            UiUtils.imageviewDrawable(binding.img2, R.drawable.progress_selected)
                        }
                    }
                    /* if (sharedHelper.role == "TEACHER"){
                        binding.name2.text = "My Class"
                        UiUtils.imageviewDrawable(binding.img2,R.drawable.my_class_selected)
                    }
                    else {
                        binding.name2.text = "Progress"
                        UiUtils.imageviewDrawable(binding.img2,R.drawable.progress_selected)
                    }*/
                }
                2 -> {
                    binding.navView.menu.getItem(0).isChecked = false
                    binding.navView.menu.getItem(1).isChecked = false
                    binding.navView.menu.getItem(2).isChecked = true
                    binding.navView.menu.getItem(3).isChecked = false
                    binding.navView.menu.getItem(3).isCheckable = false
                    binding.nav1.visibility = View.INVISIBLE
                    binding.nav2.visibility = View.INVISIBLE
                    binding.nav3.visibility = View.VISIBLE
                    binding.nav4.visibility = View.INVISIBLE
                    when(sharedHelper.role){
                        "STUDENT","PARENT" ->{
                            binding.name3.text = "Fees"
                            UiUtils.imageviewDrawable(binding.img3,R.drawable.fees_selected)
                        }
                        "TEACHER" ->{
                            binding.name3.text = "My Subject"
                            UiUtils.imageviewDrawable(binding.img3,R.drawable.subject_selected)
                        }
                        "ADMIN"   ->{
                            binding.name3.text = "Finance"
                            UiUtils.imageviewDrawable(binding.img3,R.drawable.my_class_selected)
                        }
                        else ->{
                            binding.name3.text = "Fees"
                            UiUtils.imageviewDrawable(binding.img3,R.drawable.fees_selected)
                        }
                    }
                    /*if (sharedHelper.role == "TEACHER"){
                        binding.name3.text = "My Subject"
                        UiUtils.imageviewDrawable(binding.img3,R.drawable.subject_selected)
                    }
                    else {
                        binding.name3.text = "Fees"
                        UiUtils.imageviewDrawable(binding.img3,R.drawable.fees_selected)
                    }*/
                }
                3 -> {
                    binding.navView.menu.getItem(0).isChecked = false
                    binding.navView.menu.getItem(1).isChecked = false
                    binding.navView.menu.getItem(2).isChecked = false
                    binding.navView.menu.getItem(3).isChecked = true
                    binding.nav1.visibility = View.INVISIBLE
                    binding.nav2.visibility = View.INVISIBLE
                    binding.nav3.visibility = View.INVISIBLE
                    binding.nav4.visibility = View.VISIBLE
                    binding.name4.text = "Chat"
                    UiUtils.imageviewDrawable(binding.img4, R.drawable.chat_selected)
                }
            }
        }
    }

    private fun updateMenuForRole(role: String?) {
        val menu = navView?.menu
        menu?.clear()
        when (role) {

            "ADMIN" -> {
                menu?.add(0, R.id.navigation_Home_admin, 0, "Home")?.setIcon(R.drawable.home_outline)
                menu?.add(0, R.id.navigation_Admin_User_Management, 1, "User")?.setIcon(R.drawable.subject_unselected)
                menu?.add(0, R.id.navigation_Admin_Finance, 2, "Finance")?.setIcon(R.drawable.my_class_unselected)
                menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
            }
            "STUDENT" -> {
                if (isBelow5){
                    menu?.add(0, R.id.navigation_home_new, 0, "Home")?.setIcon(R.drawable.home_outline)
                    menu?.add(0, R.id.navigation_progress, 1, "Progress")?.setIcon(R.drawable.progress_unselected)
                    menu?.add(0, R.id.navigation_fees, 2, "Fees")?.setIcon(R.drawable.fees_unselected)
                    menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
                }
                else {
                    menu?.add(0, R.id.navigation_home, 0, "Home")?.setIcon(R.drawable.home_outline)
                    menu?.add(0, R.id.navigation_progress, 1, "Progress")?.setIcon(R.drawable.progress_unselected)
                    menu?.add(0, R.id.navigation_fees, 2, "Fees")?.setIcon(R.drawable.fees_unselected)
                    menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
                }
            }
            "TEACHER" -> {
                menu?.add(0, R.id.navigation_home_teacher, 0, "Home")?.setIcon(R.drawable.home_outline)
                menu?.add(0, R.id.navigation_my_class, 1, "Progress")?.setIcon(R.drawable.my_class_unselected)
                menu?.add(0, R.id.navigation_my_subject, 2, "My Subject")?.setIcon(R.drawable.subject_unselected)
                menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
            }
            "PARENT" ->{
                menu?.add(0, R.id.navigation_home_parent, 0, "Home")?.setIcon(R.drawable.home_outline)
                menu?.add(0, R.id.navigation_parent_child_progress, 1, "Progress")?.setIcon(R.drawable.progress_unselected)
                menu?.add(0, R.id.navigation_fees, 2, "Fees")?.setIcon(R.drawable.fees_unselected)
                menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
            }
            else ->{
                menu?.add(0, R.id.navigation_home, 0, "Home")?.setIcon(R.drawable.home_outline)
                menu?.add(0, R.id.navigation_progress, 1, "Progress")?.setIcon(R.drawable.progress_unselected)
                menu?.add(0, R.id.navigation_fees, 2, "Fees")?.setIcon(R.drawable.fees_unselected)
                menu?.add(0, R.id.navigation_chat, 3, "Chat")?.setIcon(R.drawable.chat_unselected)
            }
        }
    }

    override fun onBackPressed() {
        if(binding.dialogHomework.root.visibility == View.VISIBLE){
            binding.dialogHomework.root.visibility = View.GONE
        }
        else if(binding.dialogAssignment.root.visibility == View.VISIBLE){
            binding.dialogAssignment.root.visibility = View.GONE
        }
        else if(binding.dialogProject.root.visibility == View.VISIBLE){
            binding.dialogProject.root.visibility = View.GONE
        }
        else if(binding.examDialog.root.visibility == View.VISIBLE){
            binding.examDialog.root.visibility = View.GONE
        }
        else if(binding.timeTableDialog.root.visibility == View.VISIBLE){
            binding.timeTableDialog.root.visibility = View.GONE
        }
        else if(binding.roleSwitch.root.visibility == View.VISIBLE){
            binding.roleSwitch.root.visibility = View.GONE
        }
        else{
            if (navController != null){
                val cStack = navController!!.currentBackStackEntry
                val isStart = cStack?.destination?.id == navController!!.graph.startDestinationId
                val isLast = navController!!.previousBackStackEntry == null
                if (isStart && isLast) {
//                    handleDoubleBackPress()
                    AlertDialog.Builder(this)
                        .setTitle("Exit Confirmation")
                        .setMessage("Are you sure you want to close this app?")
                        .setPositiveButton("Exit Now") { dialog, _ ->
                            dialog.dismiss()
                            super.onBackPressed()
                        }
                        .setNegativeButton("Stay Here") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                } else {
                    if (!navController!!.popBackStack()) {
                        super.onBackPressed()
                    }
                }
            }
            else {
                super.onBackPressed()
            }
        }
    }

    private fun handleDoubleBackPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastBackPressTime < doubleBackPressInterval) {
            super.onBackPressed()
        } else {
            lastBackPressTime = currentTime
//            UiUtils.showSnack("Press back again to close the app",binding!!.root)
            UiUtils.showSnack("Exit is just one tap away!",binding!!.root,false)
        }
    }

    override fun onResume() {
        super.onResume()
    }
    override fun onPause() {
        super.onPause()
    }
    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        // removeSocket()
    }
    override fun onStart() {
        super.onStart()
    }

}