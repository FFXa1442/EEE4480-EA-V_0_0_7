package com.sample.application.ea.activity.guest

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.core.graphics.Insets
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.NavigationActivity
import com.sample.application.ea.database.FirebaseControl
import com.sample.application.ea.databinding.ActivityGuestBinding

class GuestActivity :
    NavigationActivity<ActivityGuestBinding>(),
    FirebaseControl {

    override val navHostId: Int =
        R.id.nav_host_fragment_activity_guest

    override val layoutId: Int =
        R.layout.activity_guest

    override fun onBeforeCreate(
        savedInstanceState: Bundle?,
    ): Boolean {
        window.setFlags(
            FLAG_LAYOUT_NO_LIMITS,
            FLAG_LAYOUT_NO_LIMITS
        )
        return super.onBeforeCreate(savedInstanceState)
    }

    override fun onApplyWindowInsets(
        view: View,
        binding: ActivityGuestBinding,
        systemBars: Insets,
    ) {
        view.setPadding(
            systemBars.left,
            systemBars.top,
            systemBars.right,
            systemBars.bottom
        )
    }

}