package com.sample.application.ea.activity.splash.screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.ViewDataBindingActivity
import com.sample.application.ea.activity.loading.DataLoadingActivity
import com.sample.application.ea.databinding.ActivitySplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity :
    ViewDataBindingActivity<ActivitySplashScreenBinding>() {

    private var hasStarted = false

    override val layoutId: Int = R.layout.activity_splash_screen

    override fun onBeforeCreate(
        savedInstanceState: Bundle?,
    ): Boolean {
        when {
            hasStarted -> startActivity()
        }
        return hasStarted
    }

    override fun onCreate(
        binding: ActivitySplashScreenBinding,
        savedInstanceState: Bundle?,
    ) {
        Handler(Looper.getMainLooper()).postDelayed(
            this::startActivity,
            2000
        )
    }

    private fun startActivity() {
        hasStarted = true
        startActivity(
            Intent(
                this,
                DataLoadingActivity::class.java
            )
        )
        finish()
    }

}