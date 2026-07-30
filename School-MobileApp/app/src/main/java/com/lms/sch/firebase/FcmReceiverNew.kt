package com.lms.sch.firebase

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import android.graphics.Bitmap
import android.app.NotificationChannel
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.graphics.BitmapFactory
import com.lms.sch.R
import com.lms.sch.activity.DashBoardActivity
import com.lms.sch.session.Constants
import com.lms.sch.session.SharedHelper
import com.lms.sch.utils.BaseUtils
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class FcmReceiverNew : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("djhvbd0",""+ p0.notification!!)
        Log.d("djhvbd1",""+ p0.notification!!.title)
        Log.d("djhvbd4",""+p0.notification!!.body)
        Log.d("djhvbd7",""+p0.data.toString())
        Log.d("djhvbd2",""+ p0.data["type"])
        val jsonObject = JSONObject(p0.data as Map<*, *>)
        Log.d("djhvbd45",""+jsonObject)
        if(SharedHelper(applicationContext).loggedIn){
            if (p0.notification != null) {
                if(p0.notification?.imageUrl != null){
                    imageNotification(p0.notification?.title.toString(), p0.notification?.body.toString(), p0.data["image-url"].toString(),jsonObject)
                }
                else{
                    setNotification(p0.notification?.title.toString(), p0.notification?.body.toString(),jsonObject)
                }
            }
            else{
                setNotification(
                    jsonObject.optString(Constants.IntentKeys.TITLE),
                    jsonObject.optString(Constants.IntentKeys.BODY),
                    jsonObject
                )
            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        BaseUtils.sendRegistrationTokenToServer(this, p0)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setNotification(title: String, body: String, jsonObject: JSONObject) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(title)
        bigTextStyle.bigText(body)
        val intent = Intent(applicationContext, DashBoardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if(jsonObject.get("module") == "chat"){
            intent.putExtra("page",jsonObject.get("module").toString())
            intent.putExtra("id",jsonObject.get("id").toString())
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, Random.nextInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
            .setContentText(body)
            .setContentTitle(title)
            .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.notification_icon)
            .setColor(Color.parseColor(SharedHelper(this).primaryColor))
            .setSound(defaultSoundUri)
            .setStyle(bigTextStyle)
            .setFullScreenIntent(pendingIntent, true)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification.build())
    }


    private fun imageNotification(notificationTitle: String, notificationContent: String, imageUrl: String?,jsonObject: JSONObject) {
        // Let's create a notification builder object
        val builder =
            NotificationCompat.Builder(this, resources.getString(R.string.notification_channel_id))
        // Create a notificationManager object
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // If android version is greater than 8.0 then create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Create a notification channel
            val notificationChannel = NotificationChannel(
                resources.getString(R.string.notification_channel_id),
                resources.getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Set properties to notification channel
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300)

            // Pass the notificationChannel object to notificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val defaultSoundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Set the notification parameters to the notification builder object
        builder.setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setSmallIcon(R.drawable.notification_icon)
            .setColor(Color.parseColor(SharedHelper(this).primaryColor))
            .setSound(defaultSoundUri)
            .setAutoCancel(true)
        // Set the image for the notification
        if (imageUrl != null) {
            val bitmap: Bitmap? = getBitmapfromUrl(imageUrl)
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                   // .bigLargeIcon(null)
            ).setLargeIcon(bitmap)
        }
        notificationManager.notify(1, builder.build())
    }

    private fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        return try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e("awesome", "Error in getting notification image: " + e.localizedMessage)
            null
        }
    }
}
