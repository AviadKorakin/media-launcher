package com.aviadkorakin.app2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aviadkorakin.app2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Enable edge-to-edge display.
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
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


        // Launch the activity that plays the ad (and potentially awards coins).
        binding.btnWatchAd.setOnClickListener {
            startActivity(Intent(this, MediaLauncherAppActivity::class.java))
        }
    }
}