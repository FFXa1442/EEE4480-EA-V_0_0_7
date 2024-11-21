package com.sample.application.ea.functions.membersystem.activity

import android.os.Bundle
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
import androidx.annotation.IdRes
import androidx.core.graphics.Insets
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.NavigationActivity
import com.sample.application.ea.database.FirebaseControl
import com.sample.application.ea.databinding.ActivityMemberBinding

class MemberActivity :
    NavigationActivity<ActivityMemberBinding>(),
    FirebaseControl {

    companion object {
        const val ACTION: String = "ACTION"
    }

    override val navHostId: Int =
        R.id.nav_host_fragment_activity_member_system

    override val layoutId: Int =
        R.layout.activity_member

    override fun onBeforeCreate(savedInstanceState: Bundle?): Boolean {
        window.setFlags(
            FLAG_LAYOUT_NO_LIMITS,
            FLAG_LAYOUT_NO_LIMITS
        )
        return super.onBeforeCreate(savedInstanceState)
    }

    override fun onViewCreated(binding: ActivityMemberBinding, savedInstanceState: Bundle?) {
        @IdRes val actionId = intent.getIntExtra(ACTION, -1)
        require(actionId != -1) { "Invalid action ID received." }
        navigate(actionId, null)
    }

    override fun onApplyWindowInsets(
        view: View,
        binding: ActivityMemberBinding,
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
