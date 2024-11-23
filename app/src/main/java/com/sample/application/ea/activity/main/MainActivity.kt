package com.sample.application.ea.activity.main

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.application.ea.R
import com.sample.application.ea.activity.generics.NavigationActivity
import com.sample.application.ea.database.FirebaseControl
import com.sample.application.ea.databinding.ActivityMainBinding

class MainActivity :
    NavigationActivity<ActivityMainBinding>(),
    FirebaseControl {

    override val layoutId: Int =
        R.layout.activity_main

    override val navHostId: Int =
        R.id.nav_host_fragment_activity_main

    override val mainLayoutView: View
        get() = binding.main

    override val navView: BottomNavigationView
        get() = binding.navView

    override val toolbar: Toolbar
        get() = binding.toolbar

    override val appBarConfBuilder: AppBarConfiguration.Builder
        get() = AppBarConfiguration.Builder(
            R.id.nav_ticket,
            R.id.nav_category,
            R.id.nav_schedule_view
        )

    override fun setTemplateBottomMargin(value: Int) {
        binding.viewContainer.apply {
            layoutParams = (layoutParams as ConstraintLayout.LayoutParams).apply {
                bottomMargin = value
            }
        }
    }
}