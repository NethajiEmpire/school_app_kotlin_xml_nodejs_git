package com.lms.sch.activity

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.viewpager2.widget.ViewPager2
import com.lms.sch.R
import com.lms.sch.adapter.ImageAdapter
import com.lms.sch.customviews.PlayStateBroadcastingVideoView
import com.lms.sch.databinding.ActivityChatBinding
import com.lms.sch.databinding.ActivityImageViewBinding
import com.lms.sch.session.Constants
import com.lms.sch.session.TempSingleton
import com.lms.sch.utils.UiUtils
import io.socket.client.Ack
import org.json.JSONObject

class ImageViewActivity : BaseActivity() {
    private lateinit var binding: ActivityImageViewBinding
    var simpleVideoView: PlayStateBroadcastingVideoView? = null
    var mediaControls: MediaController? = null

    var handler: Handler? = null
    var runnable: Runnable? = null
    var delay:Int = 20000
    var lastDuration:Int = 0
    var isClass = false
    var classId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            onBackPressed()
        }

        val data = intent.getSerializableExtra(Constants.IntentKeys.KEY) as ArrayList<String>
        Log.d("fgvbhn",""+data[0])

        if(intent.getStringExtra(Constants.IntentKeys.KEY1) != null && intent.getStringExtra(Constants.IntentKeys.KEY1).toString().isNotEmpty()){
            classId = intent.getStringExtra(Constants.IntentKeys.KEY1).toString()
            isClass = true
        }
        else{
            isClass = false
            classId = ""
        }


        if(data[0].endsWith(".mp4")){
            binding.viewPager.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            binding.videoView.visibility = View.VISIBLE
            binding.img.visibility = View.VISIBLE
            // UiUtils.loadImage(binding.img,data[0])
            simpleVideoView = binding.videoView
            if (mediaControls == null) {
                // creating an object of media controller class
                mediaControls = MediaController(this)
                // set the anchor view for the video view
                mediaControls!!.setAnchorView(simpleVideoView)
                mediaControls!!.setMediaPlayer(simpleVideoView)
            }

            // set the media controller for video view
            // simpleVideoView.setMediaController(mediaControls)

            // set the absolute path of the video file which is going to be played
            // simpleVideoView.setVideoURI(Uri.parse("android.resource://" + packageName + "/" + R.raw.gfgvideo))
            simpleVideoView!!.setMediaController(mediaControls)
            simpleVideoView!!.setVideoURI(Uri.parse(data[0]))

            // display a toast message
            // after the video is completed
            simpleVideoView!!.setOnCompletionListener {
                // Toast.makeText(activity, "Video completed", Toast.LENGTH_LONG).show()
                // binding.img.visibility = View.VISIBLE
                // binding.progressBar.visibility = View.GONE
                if(isClass && handler != null){
                    handler!!.removeCallbacks(runnable!!)
                    handler = null
                    lastDuration = 0
                }
                true
            }

            if(isClass){
                simpleVideoView!!.setPlayPauseListener(object : PlayStateBroadcastingVideoView.PlayPauseListener {
                    override fun onPlay() {
                        Log.d("ds3",""+simpleVideoView!!.currentPosition)
                        if(handler == null) {
                            handler = Handler()
                            lastDuration = 0
                            handler!!.postDelayed(Runnable {
                                handler!!.postDelayed(runnable!!, delay.toLong())
                                // Toast.makeText(this@ClassDetailsActivity, "This method will run every 5 seconds", Toast.LENGTH_SHORT).show()
                                lastDuration += 20
                                triggerSocket(lastDuration.toString())
                            }.also { runnable = it }, delay.toLong())
                        }
                    }

                    override fun onPause() {
                        Log.d("ds4",""+simpleVideoView!!.currentPosition)
                        if(handler != null){
                            handler!!.removeCallbacks(runnable!!)
                            handler = null
                        }
                    }
                })
            }

            // display a toast message if any
            // error occurs while playing the video
            simpleVideoView!!.setOnErrorListener { mp, what, extra ->
                Toast.makeText(this, "An Error Occurred " + "While Playing Video !!!", Toast.LENGTH_LONG).show()
                // binding.img.visibility = View.VISIBLE
                // binding.progressBar.visibility = View.VISIBLE
                false
            }

            simpleVideoView!!.setOnPreparedListener { mp ->
                val lp = simpleVideoView!!.layoutParams
                val videoWidth = mp.videoWidth.toFloat()
                val videoHeight = mp.videoHeight.toFloat()
                val viewWidth = simpleVideoView!!.width.toFloat()
                lp.height = (viewWidth * (videoHeight / videoWidth)).toInt()
                simpleVideoView!!.layoutParams = lp
                if(TempSingleton.getInstance().videoSec != 0){
                    simpleVideoView!!.seekTo(TempSingleton.getInstance().videoSec)
                    simpleVideoView!!.start()
                }
                else{
                    simpleVideoView!!.start()
                }
            }

            simpleVideoView!!.setOnInfoListener { mediaPlayer, i, i2 ->
                if(i == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START){
                    binding.img.visibility = View.INVISIBLE
                    binding.progressBar.visibility = View.INVISIBLE
                }
                return@setOnInfoListener false
            }

            binding.videoView.setOnClickListener {
                /*var stopPos = simpleVideoView!!.currentPosition
                if(simpleVideoView!!.isPlaying){
                    stopPos = simpleVideoView!!.currentPosition
                    simpleVideoView!!.pause()
                }
                else{
                    simpleVideoView!!.seekTo(stopPos)
                    simpleVideoView!!.start()
                }*/
            }
            binding.img.setOnClickListener {
                // starting the video
                // binding.img.visibility = View.INVISIBLE
                // simpleVideoView.start()
            }
        }
        else{
            binding.progressBar.visibility = View.GONE
            binding.videoView.visibility = View.GONE
            binding.img.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            val adapter = ImageAdapter(this,data)
            binding.viewPager.adapter = adapter
            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.pages.text = "${position + 1} / ${data.size}"
                }
            })
        }

    }

    private fun triggerSocket(str:String){
        Log.d("videoDuraSocket2", ""+str)
        val jsonObject = JSONObject()
        jsonObject.put("event",classId)
        jsonObject.put("lastWatched",str)
        jsonObject.put("watchedDuration","20")
        jsonObject.put("user_id",sharedHelper.id)
        jsonObject.put("token",sharedHelper.token)
        mSocket!!.emit("update-video-watch", jsonObject,object : Ack {
            override fun call(vararg args: Any?) {
                Log.d("videoDuraSocket", "....$args")
            }
        })
    }


    override fun onPause() {
        super.onPause()
        if(isClass && handler != null){
            handler!!.removeCallbacks(runnable!!)
            handler = null
        }
        /*if(mediaControls != null){
            simpleVideoView!!.pause()
        }*/
    }

    override fun onResume() {
        super.onResume()
        /* if(mediaControls != null){
             simpleVideoView!!.resume()
         }*/
    }
}