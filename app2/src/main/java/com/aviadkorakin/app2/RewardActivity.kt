package com.aviadkorakin.app2

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aviadkorakin.app2.databinding.ActivityRewardBinding

class RewardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            // Retrieve insets for system bars.
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Get display cutout safe inset if available.
            val displayCutout = insets.displayCutout
            val cutoutPaddingTop = displayCutout?.safeInsetTop ?: 0

            // Calculate combined padding.
            val left = v.paddingLeft + systemBarsInsets.left
            val top = v.paddingTop + systemBarsInsets.top + cutoutPaddingTop
            val right = v.paddingRight + systemBarsInsets.right
            val bottom = v.paddingBottom + systemBarsInsets.bottom

            // Apply combined padding.
            v.setPadding(left, top, right, bottom)
            insets
        }


        // Determine the reward message based on the video playback status.
        val videoInterrupted = intent.getBooleanExtra("VIDEO_INTERRUPTED", false)
        val message = if (videoInterrupted) {
            "Uh oh! The video got interrupted. Tap below to try again and earn more coins."
        } else {
            "Great job! You've earned 50 coins. Tap below to cash out your coins."
        }
        binding.tvReward.text = message

        // Apply a fade-in animation to the reward message.
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 800  // duration in milliseconds
            fillAfter = true
        }
        binding.tvReward.startAnimation(fadeIn)

        // Set up the action button based on the video playback status.
        if (videoInterrupted) {
            binding.btnAction.text = "Try Again"
            binding.btnAction.setOnClickListener {
                // Navigate back to MainActivity to try earning coins again.
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        } else {
            binding.btnAction.text = "Cash Out"
            binding.btnAction.setOnClickListener {
                // TODO: Add your cash-out logic for coins here.
                finish()
            }
        }
    }
}