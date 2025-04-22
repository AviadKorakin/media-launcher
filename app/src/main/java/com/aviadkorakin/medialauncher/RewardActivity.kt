package com.aviadkorakin.medialauncher

import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aviadkorakin.medialauncher.databinding.ActivityRewardBinding

class RewardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Determine the reward message based on the video playback status.
        val videoInterrupted = intent.getBooleanExtra("VIDEO_INTERRUPTED", false)
        val message = if (videoInterrupted) {
            "Oh no! The video was interrupted. Please try again to earn your reward."
        } else {
            "Congratulations! You just won \$0.02 dollars. Let's cash out!"
        }

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

        binding.tvReward.text = message

        // Apply a fade-in animation to the reward text.
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 800  // duration in milliseconds
            fillAfter = true
        }
        binding.tvReward.startAnimation(fadeIn)

        // Set up the button depending on the video playback status.
        if (videoInterrupted) {
            binding.btnContinue.text = "Try Again"
            binding.btnContinue.setOnClickListener {
                // Navigate back to MainActivity to try again.
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            binding.btnContinue.text = "Cash Out"
            binding.btnContinue.setOnClickListener {
                // Insert your cash-out logic here if needed.
                finish()
            }
        }
    }
}