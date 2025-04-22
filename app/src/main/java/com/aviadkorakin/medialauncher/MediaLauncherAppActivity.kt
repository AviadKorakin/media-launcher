package com.aviadkorakin.medialauncher

import android.content.Intent
import android.net.Uri
import com.aviadkorakin.common.MediaLauncherActivity
import androidx.core.net.toUri

class MediaLauncherAppActivity : MediaLauncherActivity() {

    override fun getVideoUri(): Uri {
        return "android.resource://$packageName/${R.raw.intro_video}".toUri()
    }

    override fun getVideoPlaybackCallback(): VideoPlaybackCallback {
        return object : VideoPlaybackCallback {
            override fun onVideoCompleted() {
                // Video finished normally; launch MainActivity without flag.
                startActivity(Intent(this@MediaLauncherAppActivity, RewardActivity::class.java))
            }

            override fun onVideoInterrupted() {
                // Video was interrupted; signal MainActivity using an Intent extra.
                val intent = Intent(this@MediaLauncherAppActivity, RewardActivity::class.java)
                intent.putExtra("VIDEO_INTERRUPTED", true)
                startActivity(intent)
            }
        }
    }
}