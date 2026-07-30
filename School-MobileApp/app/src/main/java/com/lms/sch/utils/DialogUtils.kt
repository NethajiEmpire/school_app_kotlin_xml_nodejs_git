package com.lms.sch.utils

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.view.*
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.lms.sch.R
//import com.tactv.travels.activity.SplashActivity
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessagingService
import com.lms.sch.activity.BaseActivity
import com.lms.sch.interfaces.OnClickListener
import com.lms.sch.session.TempSingleton


@SuppressLint("UnspecifiedImmutableFlag")
object DialogUtils {
    private var loaderDialog: Dialog? = null
    var dialog: BottomSheetDialog? = null

//    fun getReportDialog(context: Context, dialogBinding: DialogReportBinding): BottomSheetDialog {
//        dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
//        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog!!.setContentView(dialogBinding.root)
//        val window: Window? = dialog!!.window
//        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        val mBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(dialogBinding.root.parent as View)
//        mBehavior.state = BottomSheetBehavior.STATE_EXPANDED;
//        //  mBehavior.peekHeight = (context.resources.displayMetrics.heightPixels * 0.4).toInt()
////        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
////        int height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
////        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
//        return dialog!!
//    }

    fun showImageVideoDialog(activity: Activity,onClickListener: OnClickListener) {
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle(activity.getString(R.string.choose_your_option))
        val items = arrayOf("Images","Videos")
        //val items = arrayOf("Gallery")
        alertBuilder.setItems(items) { _, which ->
            when (which) {
                0 -> {
                    onClickListener.onClickItem(0)
                }
                1 -> {
                    onClickListener.onClickItem(1)
                }
            }
        }

        val alert = alertBuilder.create()
        val window = alert.window
        if (window != null) {
            //window.attributes.windowAnimations = R.style.DialogAnimation
        }
        alert.show()
    }

    fun showNewCustomDialog(context: BaseActivity?, title: String?, content: String?, cancelable: Boolean = true, positiveButtonText: String? = null, positiveButtonClickListener: DialogInterface.OnClickListener? = null, negativeButtonText: String? = null, negativeButtonClickListener: DialogInterface.OnClickListener? = null) {
        if (context != null && title != null && content != null) {
            if (context.mCustomDialog == null || !context.mCustomDialog!!.isShowing) {
                val builder = androidx.appcompat.app.AlertDialog.Builder(context)
                builder.setTitle(title)
                builder.setMessage(content)
                builder.setCancelable(cancelable)
                if (positiveButtonText != null && positiveButtonClickListener != null) {
                    builder.setPositiveButton(positiveButtonText, positiveButtonClickListener)
                } else {
                    builder.setPositiveButton(context.resources.getString(R.string.app_name)) { dialogInterface: DialogInterface, _: Int ->
                        dialogInterface.dismiss()
                    }
                }
                if (negativeButtonText != null && negativeButtonClickListener != null) {
                    builder.setNegativeButton(negativeButtonText, negativeButtonClickListener)
                }
                context.mCustomDialog = builder.create()
                context.mCustomDialog?.show()
            }
        }
    }

    private fun dismissCustomDialog(context: BaseActivity) {
        if (context.mCustomDialog != null && context.mCustomDialog!!.isShowing) {
            context.mCustomDialog?.dismiss()
        }
    }

    fun initialNotify(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentNotify: Int = BaseUtils.getFormattedDate(java.time.LocalDate.now().toString(), "yyyy-MM-dd", "dd").toInt()
            if (SharedHelper(context).lastNotify != currentNotify) {
                SharedHelper(context).lastNotify = currentNotify
                showNotify(context)
            }
        }
        else {
            val currentNotify: Int = BaseUtils.getFormattedDate(BaseUtils.getCurrentDate("yyyy-MM-dd").toString(), "yyyy-MM-dd", "dd").toInt()
            if (SharedHelper(context).lastNotify != currentNotify) {
                SharedHelper(context).lastNotify = currentNotify
                showNotify(context)
            }
        }
    }
    fun showNotify(context: Context) {
//        createNotification(pendingIntent(context, SplashActivity()), "Welcome", "By PieChips",null, context)
    }
    private fun pendingIntent(context: Context,activity: Activity):PendingIntent{
        when {
//            activity.javaClass.simpleName.equals(SplashActivity().javaClass.simpleName) -> {
//                val intent = Intent(context, activity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
//            }
            else -> {
                val intent = Intent(context, activity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE)
            }
        }
    }
    private fun createNotification(pendingIntent: PendingIntent, title: String, body: String, imageUrl: String?, context: Context){
        if(imageUrl != null && imageUrl != "" && imageUrl.isNotEmpty()){
            createImageNotification(pendingIntent, title, body, imageUrl,context)
        }
        else{
            simpleNotification(pendingIntent, title, body, context)
        }
    }
    private fun simpleNotification(pendingIntent: PendingIntent, notificationTitle: String, notificationContent: String, context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setContentText(notificationContent)
                .setContentTitle(notificationTitle)
                .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setStyle(
                    NotificationCompat.BigTextStyle().setBigContentTitle(notificationTitle)
                        .bigText(notificationContent)
                )
                .setContentIntent(pendingIntent)
                //.setSmallIcon(R.drawable.notification_icon)
                .setColor(context.getColor(R.color.colorPrimary))
                .setFullScreenIntent(pendingIntent, true)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }
    private fun createImageNotification(pendingIntent: PendingIntent, title: String, body: String, imageUrl: String, context: Context) {
        val bitmap = arrayOf<Bitmap?>(null)
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    bitmap[0] = resource
                    imageNotification(pendingIntent, context, title, body, bitmap[0]!!)
                }

                override fun onLoadCleared(@Nullable placeholder: Drawable?) {}
            })
    }
    fun imageNotification(pendingIntent: PendingIntent, context: Context, notificationTitle: String, notificationContent: String, imageUrl: Bitmap?) {
        val builder =
            NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
        val notificationManager =
            context.getSystemService(FirebaseMessagingService.NOTIFICATION_SERVICE) as NotificationManager
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        builder.setContentTitle(notificationTitle)
            .setContentText(notificationContent)
           // .setSmallIcon(R.drawable.notification_icon)
            .setColor(context.getColor(R.color.colorPrimary))
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        // Set the image for the notification
        if (imageUrl != null) {
            // val bitmap: Bitmap? = getBitmapFromUrl(imageUrl)
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(imageUrl)
                    .bigLargeIcon(imageUrl)
            ).setLargeIcon(imageUrl)
        }
        notificationManager.notify(1, builder.build())
    }

    fun showPictureDialog(activity: Activity) {
        val alertBuilder = AlertDialog.Builder(activity)
        alertBuilder.setTitle(activity.getString(R.string.choose_your_option))
        val items = arrayOf(activity.getString(R.string.gallery), activity.getString(R.string.camera))
        //val items = arrayOf("Gallery")
        alertBuilder.setItems(items) { _, which ->
            when (which) {
                0 -> if (BaseUtils.isPermissionsEnabled(activity, Constants.IntentKeys.GALLERY)) {
                    TempSingleton.getInstance().isMultiple = false
                    TempSingleton.getInstance().isImage = true
                    TempSingleton.getInstance().isVideo = false
                    TempSingleton.getInstance().isFile = false
//                    BaseUtils.openGallery(activity)
                } else {
                    if (BaseUtils.isDeniedPermission(activity, Constants.IntentKeys.GALLERY)) {
                        BaseUtils.displayManuallyEnablePermissionsDialog(activity, Constants.IntentKeys.GALLERY, null)
                    } else {
                        BaseUtils.permissionsEnableRequest(activity, Constants.IntentKeys.GALLERY)
                    }
                }
                1 -> if (BaseUtils.isPermissionsEnabled(activity, Constants.IntentKeys.CAMERA)) {
//                    BaseUtils.openCamera(activity)
                } else {
                    if (BaseUtils.isDeniedPermission(activity, Constants.IntentKeys.CAMERA)) {
                        BaseUtils.displayManuallyEnablePermissionsDialog(activity, Constants.IntentKeys.CAMERA, null)
                    } else {
                        BaseUtils.permissionsEnableRequest(activity, Constants.IntentKeys.CAMERA)
                    }
                }
            }
        }

        val alert = alertBuilder.create()
        val window = alert.window
        if (window != null) {
            //window.attributes.windowAnimations = R.style.DialogAnimation
        }
        alert.show()
    }
    fun showLoader(activity: Activity) {
        if (loaderDialog != null) {
            if (loaderDialog!!.isShowing && !activity.isFinishing) {
                loaderDialog!!.dismiss()
            }
        }

        loaderDialog = Dialog(activity)
       // loaderDialog = Dialog(context,android.R.style.Theme_Light)
        loaderDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loaderDialog?.setCancelable(false)
        loaderDialog?.setCanceledOnTouchOutside(false)
        loaderDialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        loaderDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val inflater = LayoutInflater.from(activity)
        val view = inflater?.inflate(R.layout.dialog_loader, null)
        if (view != null) {
            loaderDialog!!.setContentView(view)
        }

        loaderDialog!!.findViewById<com.airbnb.lottie.LottieAnimationView>(R.id.anim_view).playAnimation()
        // Glide.with(context).load(R.drawable.loader_common).into(loaderDialog!!.findViewById(R.id.image))

        if (!loaderDialog?.isShowing!!) {
            loaderDialog?.show()
        }
    }

    fun dismissLoader() {
        if (loaderDialog != null) {
            if (loaderDialog!!.isShowing) {
                loaderDialog!!.dismiss()
            }
        }
    }
}