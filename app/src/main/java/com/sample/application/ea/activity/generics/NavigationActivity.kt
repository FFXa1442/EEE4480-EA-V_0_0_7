package com.sample.application.ea.activity.generics

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sample.application.ea.widget.state.BottomNavViewState
import com.sample.application.ea.widget.state.ToolbarState

abstract class NavigationActivity<TBinding> :
    ViewDataBindingActivity<TBinding>,
    ToolbarState,
    BottomNavViewState where TBinding : ViewDataBinding {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private var appBarConfiguration: AppBarConfiguration? = null

    @get:IdRes
    protected abstract val navHostId: Int

    protected open val navView: BottomNavigationView? = null

    protected open val toolbar: Toolbar? = null

    protected open val appBarConfBuilder: AppBarConfiguration.Builder? = null

    private val navController: NavController
        get() = findNavController(navHostId)

    fun navigate(
        @IdRes id: Int,
        args: Bundle? = null,
    ) = navController.navigate(id, args)

    fun navigateUp() {
        appBarConfiguration.let { conf ->
            when (conf) {
                null -> {
                    navController.navigateUp()
                }

                else -> {
                    navController.navigateUp(conf)
                }
            }
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        toolbar?.let {
            setSupportActionBar(it)
        }

        navController.let { navCtrl ->

            appBarConfBuilder?.build()?.apply {
                appBarConfiguration = this
                setupActionBarWithNavController(
                    navCtrl,
                    this
                )
            }

            supportActionBar?.apply {
                setDisplayShowTitleEnabled(false)
            }

            navView?.apply {
                setupWithNavController(navCtrl)
            }
        }
    }

    override fun showToolbar() {
        toolbar?.apply {
            visibility = View.VISIBLE
        }
    }

    override fun hideToolbar() {
        toolbar?.apply {
            visibility = View.GONE
        }
    }

    protected open fun setTemplateBottomMargin(
        value: Int,
    ) = Unit

    override fun showNavView() {
        navView?.apply {
            visibility = View.VISIBLE
            post {
                setTemplateBottomMargin(height)
            }
        }
    }

    override fun hideNavView() {
        navView?.apply {
            visibility = View.GONE
            post {
                setTemplateBottomMargin(0)
            }
        }
    }

}