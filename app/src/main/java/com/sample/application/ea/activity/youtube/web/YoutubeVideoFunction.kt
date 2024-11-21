package com.sample.application.ea.activity.youtube.web

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.View
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.sample.application.ea.databinding.ActivityYoutubeBinding
import com.sample.application.ea.extension.getValue
import com.sample.application.ea.extension.setValue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


fun YoutubeActivity.setupVideo(
    binding: ActivityYoutubeBinding,
    videoId: String,
    youTubePlayer: AtomicReference<YouTubePlayer?>,
    isFullscreen: AtomicBoolean,
) {
    var ytPlayer: YouTubePlayer? by youTubePlayer

    lifecycle.addObserver(binding.youtubePlayerView)

    binding.youtubePlayerView.addFullscreenListener(object : FullscreenListener {

        override fun onEnterFullscreen(
            fullscreenView: View,
            exitFullscreen: () -> Unit,
        ) {
            onEnterFullscreen(
                binding,
                fullscreenView,
                isFullscreen
            )
        }

        override fun onExitFullscreen() {
            onExitFullscreen(
                binding,
                isFullscreen
            )
        }

    })

    val playerListener = object : AbstractYouTubePlayerListener() {

        override fun onReady(youTubePlayer: YouTubePlayer) {
            ytPlayer = youTubePlayer
            youTubePlayer.loadVideo(
                videoId,
                0f
            )
        }

    }

    val playerOptions = IFramePlayerOptions.Builder()
        .controls(1)
        .fullscreen(1)
        .build()

    binding.youtubePlayerView.apply {
        enableAutomaticInitialization = false
        initialize(playerListener, playerOptions)
    }

}

internal fun YoutubeActivity.onEnterFullscreen(
    binding: ActivityYoutubeBinding,
    view: View,
    isFullscreen: AtomicBoolean,
) {
    var flag: Boolean by isFullscreen
    flag = true

    binding.appbarLayout.visibility = View.GONE
    binding.fullscreenContainer.apply {
        visibility = View.VISIBLE
        addView(view)
    }

    WindowInsetsControllerCompat(window, binding.rootView).apply {
        hide(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    when {
        resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE ->
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }

}

internal fun YoutubeActivity.onExitFullscreen(
    binding: ActivityYoutubeBinding,
    isFullscreen: AtomicBoolean,
) {
    var flag: Boolean by isFullscreen
    flag = false

    binding.appbarLayout.visibility = View.VISIBLE
    binding.fullscreenContainer.visibility = View.GONE
    binding.fullscreenContainer.removeAllViews()

    WindowInsetsControllerCompat(window, binding.rootView).apply {
        show(WindowInsetsCompat.Type.systemBars())
    }

    when {
        resources.configuration.orientation != Configuration.ORIENTATION_PORTRAIT -> {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}