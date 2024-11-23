package com.sample.application.ea.activity.youtube.web

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.Insets
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.ViewDataBindingActivity
import com.sample.application.ea.databinding.ActivityYoutubeBinding
import com.sample.application.ea.extension.getValue
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


class YoutubeActivity :
    ViewDataBindingActivity<ActivityYoutubeBinding>() {

    companion object {
        const val TAG = "YOUTUBE_ID"
    }

    private val youTubePlayer = AtomicReference<YouTubePlayer?>(null)

    private val isFullscreen = AtomicBoolean(false)

    override val layoutId: Int = R.layout.activity_youtube

    override fun onSetRequestedOrientation() {
    }

    override fun onCreateView(
        binding: ActivityYoutubeBinding,
        savedInstanceState: Bundle?,
    ) {

        val flag: Boolean by isFullscreen
        val ytPlayer: YouTubePlayer? by youTubePlayer

        val url = intent.getStringExtra(TAG)!!
        setupVideo(binding, url, youTubePlayer, isFullscreen)
        binding.exitButton.setOnClickListener { finish() }

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                when {
                    flag -> {
                        ytPlayer?.toggleFullscreen()
                    }

                    else -> {
                        finish()
                    }
                }
            }

        })
    }

    override fun onApplyWindowInsets(
        view: View,
        binding: ActivityYoutubeBinding,
        systemBars: Insets,
    ) {
        super.onApplyWindowInsets(view, binding, systemBars)
        onApplyWindowInsets(binding, systemBars.top)
    }

    private fun onApplyWindowInsets(
        binding: ActivityYoutubeBinding,
        sysStatusBarHeight: Int,
    ) {
        binding.appbarLayout.apply {
            val params = layoutParams as ConstraintLayout.LayoutParams
            params.topMargin = sysStatusBarHeight
            layoutParams = params
        }
    }

    @SuppressLint("SwitchIntDef")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val flag: Boolean by isFullscreen
        val ytPlayer: YouTubePlayer? by youTubePlayer

        when (newConfig.orientation) {
            Configuration.ORIENTATION_LANDSCAPE ->
                when {
                    !flag -> ytPlayer!!.toggleFullscreen()
                }

            Configuration.ORIENTATION_PORTRAIT ->
                when {
                    flag -> ytPlayer!!.toggleFullscreen()
                }
        }

    }

}