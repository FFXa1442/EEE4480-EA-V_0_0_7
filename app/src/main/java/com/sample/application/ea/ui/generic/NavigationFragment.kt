package com.sample.application.ea.ui.generic

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.sample.application.ea.database.FirebaseControl
import com.sample.application.ea.widget.state.BottomNavViewState
import com.sample.application.ea.widget.state.ToolbarState

abstract class NavigationFragment<TBinding, TParentActivity> :
    ViewDataBindingFragment<TBinding, TParentActivity>
        where TBinding : ViewDataBinding,
              TParentActivity : AppCompatActivity,
              TParentActivity : FirebaseControl,
              TParentActivity : ToolbarState,
              TParentActivity : BottomNavViewState {

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @CallSuper
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setToolbarState()
        setBottomNavViewState()
    }

    protected open fun setToolbarState() = parentActivity.hideToolbar()

    protected open fun setBottomNavViewState() = parentActivity.showNavView()

}