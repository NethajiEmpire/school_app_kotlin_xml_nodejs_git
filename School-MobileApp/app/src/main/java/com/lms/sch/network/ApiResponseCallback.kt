package com.lms.sch.network

import android.content.Context
import com.lms.sch.activity.BaseActivity
import com.lms.sch.utils.BaseUtils.disableUserInteraction
import com.lms.sch.utils.BaseUtils.enableUserInteraction
import com.lms.sch.models.BaseModel
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

open class ApiResponseCallback<R : BaseModel>(val context: Context, val isDisableInteraction: Boolean) : Observer<R> {

    override fun onSubscribe(disposable: Disposable) {
        try {
            if (context is BaseActivity)
                context.mCompositeDisposable.add(disposable)
            if (isDisableInteraction)
                disableUserInteraction(context)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNext(responseModel: R) {
        try {
            enableUserInteraction(context)
            if (!(responseModel as BaseModel).success) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onError(e: Throwable) {
        enableUserInteraction(context)
        e.printStackTrace()
    }

    override fun onComplete() {
        enableUserInteraction(context)
    }
}