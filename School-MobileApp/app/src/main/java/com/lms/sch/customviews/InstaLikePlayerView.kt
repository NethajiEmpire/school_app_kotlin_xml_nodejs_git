package com.lms.sch.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Looper
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.lms.sch.R
import com.lms.sch.session.SharedHelper
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AdViewProvider
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.Assertions

class InstaLikePlayerView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? =  /* attrs= */null,
    defStyleAttr: Int =  /* defStyleAttr= */0
) : FrameLayout(
    context!!, attrs, defStyleAttr
), AdViewProvider {
    private var videoSurfaceView: View?
    private var player: Player? = null
    private var textureViewRotation = 0
    private var isTouching = false
    private var isPlay = false

    /**
     * Returns the player currently set on this view, or null if no player is set.
     */
    private fun getPlayer(): Player? {
        return player
    }

    /**
     * Set the [Player] to use.
     *
     *
     * To transition a [Player] from targeting one view to another, it's recommended to use
     * [.switchTargetView] rather than this method. If you do
     * wish to use this method directly, be sure to attach the player to the new view *before*
     * calling `setPlayer(null)` to detach it from the old one. This ordering is significantly
     * more efficient and may allow for more seamless transitions.
     *
     * @param player The [Player] to use, or `null` to detach the current player. Only
     * players which are accessed on the main thread are supported (`player.getApplicationLooper() == Looper.getMainLooper()`).
     */
    private fun setPlayer(player: Player?) {
        Assertions.checkState(Looper.myLooper() == Looper.getMainLooper())
        Assertions.checkArgument(
            player == null || player.applicationLooper == Looper.getMainLooper()
        )
        if (this.player === player) {
            return
        }
        val oldPlayer = this.player
        if (oldPlayer != null) {
            oldPlayer.clearVideoSurfaceView(videoSurfaceView as SurfaceView?)
            // oldPlayer.videoComponent?.clearVideoSurfaceView(videoSurfaceView as SurfaceView?)
        }
        this.player = player
        if (player != null) {
            player.setVideoSurfaceView(videoSurfaceView as SurfaceView?)
            // player.videoComponent?.setVideoSurfaceView(videoSurfaceView as SurfaceView?)
        }
        else {

        }
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        // Work around https://github.com/google/ExoPlayer/issues/3160.
        videoSurfaceView?.visibility = visibility

    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (player != null && player!!.isPlayingAd) {
            return super.dispatchKeyEvent(event)
        }
        val isDpadKey = isDpadKey(event.keyCode)
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (player == null) {
            false
        } else when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouching = true
                true
            }
            MotionEvent.ACTION_UP -> {
                if (isTouching) {
                    isTouching = false
                    performClick()
                    return true
                }
                false
            }
            else -> false
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        return false
    }

    override fun onTrackballEvent(ev: MotionEvent): Boolean {
        return false
    }

    override fun getAdViewGroup(): ViewGroup? {
        return null
    }

    fun getAdOverlayViews(): Array<View?> {
        return arrayOfNulls(0)
    }

    @SuppressLint("InlinedApi")
    private fun isDpadKey(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_UP_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_DOWN_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_UP_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_CENTER
    }

    init {
        if (isInEditMode) {
            videoSurfaceView = null

        } else {
            val playerLayoutId = R.layout.exo_simple_player_view
            LayoutInflater.from(context).inflate(playerLayoutId, this)
            descendantFocusability = FOCUS_AFTER_DESCENDANTS

            // Content frame.
            videoSurfaceView = findViewById(R.id.surface_view)
            init()
        }
    }

    private var lastPos: Long? = 0
    private var videoUri: Uri? = null
    /*
        private var cacheDataSourceFactory = CacheDataSourceFactory(
            StaticData.simpleCache,
            DefaultHttpDataSourceFactory(
                Util.getUserAgent(
                    context!!, context.getString(
                        R.string.app_name
                    )
                )
            ),
            CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR
        )*/

    fun init() {
        reset()

        /*Setup player + Adding Cache Directory*/
        val simpleExoPlayer = SimpleExoPlayer.Builder(context).build()
        simpleExoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_READY) {
                    //simpleExoPlayer.seekTo(lastPos!!)
                    alpha = 1f
                }
            }

        })
        simpleExoPlayer.playWhenReady = false
        setPlayer(simpleExoPlayer)
    }

    /**
     * This will reuse the player and will play new URI we have provided
     */
    fun startPlaying() {
        val mediaItem = com.google.android.exoplayer2.MediaItem.fromUri(videoUri!!)
        val mediaSource = ProgressiveMediaSource.Factory(CacheDataSource.Factory()).createMediaSource(mediaItem)
        (player as SimpleExoPlayer).prepare(mediaSource)

        player?.seekTo(lastPos!!)
        player?.playWhenReady = true

        if(SharedHelper(context).isMute){
            player?.volume = 0f
        }
        else{
            player?.volume = 1f
        }

        isPlay = true
    }

    /**
     * This will stop the player, but stopping the player shows blackScreen
     * so to cover that we set alpha to 0 of player
     * and lastFrame of player using imageView over player to make it look like paused player
     *
     * (If we will not stop the player, only pause , then it can cause memory issue due to overload of player
     * and paused player can not be payed with new URL, after stopping the player we can reuse that with new URL
     *
     */
    fun removePlayer() {
        getPlayer()?.playWhenReady = false
        lastPos = getPlayer()?.currentPosition
        reset()
        getPlayer()?.stop(true)
        isPlay = false
    }

    fun reset() {
        // This will prevent surface view to show black screen,
        // and we will make it visible when it will be loaded
        alpha = 0f
    }

    fun setVideoUri(uri: Uri?) {
        this.videoUri = uri
    }

    fun mute(value : Boolean){
        if(value){
            player!!.volume = 0f
        }
        else{
            player!!.volume = 1f
        }
    }

    fun pausePlay(){
        player!!.playWhenReady = !player!!.isPlaying
    }

    fun pause(){
        player!!.playWhenReady = false
        isPlay = false
    }

    fun resume(){
        player!!.playWhenReady = true
        isPlay = true
    }

    fun isPlaying():Boolean{
        return player!!.isPlaying
    }

}