package com.aviadkorakin.common
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.aviadkorakin.common.databinding.ActivityMediaLauncherBinding

abstract class MediaLauncherActivity : AppCompatActivity() {

    // View binding instance for activity_media_launcher.xml
    private lateinit var binding: ActivityMediaLauncherBinding
    private var player: ExoPlayer? = null

    // Handler for updating the circular progress indicator.
    private val progressHandler = Handler(Looper.getMainLooper())

    /**
     * Callback interface to notify whether the video played completely
     * or was interrupted by the user.
     */
    interface VideoPlaybackCallback {
        fun onVideoCompleted()
        fun onVideoInterrupted()
    }

    // Subclasses must implement these.
    abstract fun getVideoUri(): Uri
    abstract fun getVideoPlaybackCallback(): VideoPlaybackCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Adjust the skip container position for devices with status bar overlays.
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { _, insets ->
            // Get the status bar insets.
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val lp = binding.skipContainer.layoutParams as? androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
                ?: binding.skipContainer.layoutParams as? android.widget.RelativeLayout.LayoutParams
            lp?.topMargin = statusBarHeight
            binding.skipContainer.layoutParams = lp
            insets
        }

        // Set maximum progress value programmatically.
        binding.circularProgress.max = 100

        // Initialize Media3 ExoPlayer and start playback.
        initializePlayer()

        // Set up the click listener on the "X" icon.
        binding.ivSkip.setOnClickListener {
            player?.stop()
            getVideoPlaybackCallback().onVideoInterrupted()
            finish()
        }

        // Start updating the circular progress indicator.
        updateProgress()
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this).build().apply {
            binding.playerView.player = this
            // Create and set a MediaItem based on the provided URI.
            setMediaItem(MediaItem.fromUri(getVideoUri()))
            prepare()
            play()
            // Listen for state changes to detect when playback finishes.
            addListener(object : androidx.media3.common.Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == ExoPlayer.STATE_ENDED) {
                        // Ensure the circular progress is complete.
                        binding.circularProgress.progress = binding.circularProgress.max
                        getVideoPlaybackCallback().onVideoCompleted()
                        finish()
                    }
                }
            })
        }
    }

    // Periodically update the circular progress indicator.
    private fun updateProgress() {
        progressHandler.postDelayed(object : Runnable {
            override fun run() {
                player?.let { exoPlayer ->
                    if (exoPlayer.isPlaying) {
                        val duration = exoPlayer.duration
                        val position = exoPlayer.currentPosition
                        if (duration > 0) {
                            // Calculate progress out of 100.
                            val progress = ((position * 100) / duration).toInt()
                            binding.circularProgress.progress = progress
                        }
                    }
                    progressHandler.postDelayed(this, 250)
                }
            }
        }, 250)
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
        progressHandler.removeCallbacksAndMessages(null)
    }
}