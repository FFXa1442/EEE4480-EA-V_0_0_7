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
import com.sample.application.ea.utilities.TryUtil.tryNonNull
import com.sample.application.ea.utilities.TryUtil.tryNonNullElse
import com.sample.application.ea.widget.state.BottomNavViewState
import com.sample.application.ea.widget.state.ToolbarState

abstract class NavigationActivity<TBinding> :
    ViewDataBindingActivity<TBinding>,
    ToolbarState,
    BottomNavViewState
        where TBinding : ViewDataBinding {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private var appBarConfiguration: AppBarConfiguration? = null

    @get:IdRes
    protected abstract val navHostId: Int

    protected open val navView: BottomNavigationView? = null

    protected open val toolbar: Toolbar? = null

    protected open val appBarConfBuilder: AppBarConfiguration.Builder? = null

    protected val navController: NavController
        get() = findNavController(navHostId)

    fun navigate(
        @IdRes id: Int,
        args: Bundle? = null
    ) = navController.navigate(id, args)

    fun navigateUp() = tryNonNullElse(
        appBarConfiguration,
        navController::navigateUp,
        navController::navigateUp
    )

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) =
        super.onCreate(savedInstanceState).apply {
            tryNonNull(toolbar) {
                setSupportActionBar(it)
            }

            navController.let { navCtrl ->
                tryNonNull(appBarConfBuilder?.build()) { conf ->
                    appBarConfiguration = conf
                    setupActionBarWithNavController(
                        navCtrl,
                        conf
                    )
                }

                tryNonNull(supportActionBar) { appBar ->
                    appBar.setDisplayShowTitleEnabled(false)
                }

                tryNonNull(navView) { nav ->
                    nav.setupWithNavController(navCtrl)
                }
            }
        }

    override fun showToolbar() = tryNonNull(
        toolbar
    ) {
        it.visibility = View.VISIBLE
    }

    override fun hideToolbar() = tryNonNull(
        toolbar
    ) {
        it.visibility = View.GONE
    }

    protected open fun setTemplateBottomMargin(
        value: Int
    ) = Unit

    override fun showNavView() = tryNonNull(
        navView
    ) {
        it.visibility = View.VISIBLE
        it.post {
            setTemplateBottomMargin(it.height)
        }
    }

    override fun hideNavView() = tryNonNull(
        navView
    ) {
        it.visibility = View.GONE
        it.post {
            setTemplateBottomMargin(0)
        }
    }

}