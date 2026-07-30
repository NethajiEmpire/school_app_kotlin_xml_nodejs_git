package com.lms.sch.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Coroutien {

    fun iOWorker (work : suspend (() -> Unit)){
        CoroutineScope(Dispatchers.IO).launch {
            work()
        }
    }

}