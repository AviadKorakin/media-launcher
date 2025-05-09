https://github.com/user-attachments/assets/39598b73-f08f-4293-9a7c-b11fcb01cbcd

https://github.com/user-attachments/assets/0c719e23-a9d9-4fda-8882-d30618ed334d


## System Description

This library exposes a plug‑and‑play ExoPlayer‑based component that lets you embed video ads in your Android app with minimal setup and no Google Ads dependencies.  
It provides a central “X” skip button overlay that users can tap to exit the ad at any time, and automatically detects when playback completes or is interrupted to hand control back to your host activity.

### Architecture

- **MediaLauncherActivity**  
  A base `AppCompatActivity` that sets up edge‑to‑edge insets, initializes ExoPlayer, displays a circular progress bar, and wires up a central skip (“X”) button.  
- **MediaLauncherAppActivity**  
  Your subclass: override `getVideoUri()` to point at your ad, and `getVideoPlaybackCallback()` to handle “completed” or “interrupted” events.  

### Features

- **Plug‑and‑play integration**: simply extend `MediaLauncherActivity` and supply your URI.  
- **Custom skip UI**: central “X” overlay with safe‑inset handling for notches and status bars.  
- **Progress indicator**: circular bar updated every 250 ms.  
- **Reward flow**: post‑ad callback logic for retry or reward actions.

### Create your own ad activity
```kotlin

class MediaLauncherAppActivity : MediaLauncherActivity() {
    override fun getVideoUri(): Uri =
        "android.resource://$packageName/${R.raw.intro_video}".toUri()

    override fun getVideoPlaybackCallback(): VideoPlaybackCallback =
        object : VideoPlaybackCallback {
            override fun onVideoCompleted() { /* ... */ }
            override fun onVideoInterrupted() { /* ... */ }
        }
}


	3.	From your main activity, launch it with:

startActivity(Intent(this, MediaLauncherAppActivity::class.java))
```
