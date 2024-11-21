package com.sample.application.ea.activity.generics

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class ViewDataBindingActivity<TBinding> :
    AppCompatActivity where TBinding : ViewDataBinding {

    protected lateinit var binding: TBinding

    @get:LayoutRes
    protected abstract val layoutId: Int

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    protected open fun onBeforeCreate(
        savedInstanceState: Bundle?
    ): Boolean = false

    @SuppressLint("SourceLockedOrientationActivity")
    protected open fun onSetRequestedOrientation() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    protected open val mainLayoutView: View
        get() = binding.root

    @CallSuper
    override fun onCreate(
        savedInstanceState: Bundle?
    ) = super.onCreate(savedInstanceState).let {
        enableEdgeToEdge()
        onSetRequestedOrientation()
        when {
            onBeforeCreate(savedInstanceState) -> return
            else -> {
                binding = DataBindingUtil.setContentView<TBinding>(
                    this,
                    layoutId
                ).apply {
                    lifecycleOwner = this@ViewDataBindingActivity
                }
                onCreate(binding, savedInstanceState)
                onCreateView(binding, savedInstanceState)
                setContentView(binding.root)
                onViewCreated(binding, savedInstanceState)
                ViewCompat.setOnApplyWindowInsetsListener(mainLayoutView) { view, insets ->

                    this@ViewDataBindingActivity.onApplyWindowInsets(
                        view,
                        binding,
                        insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                        )
                    )

                    insets
                }
            }
        }
    }

    protected open fun onApplyWindowInsets(
        view: View,
        binding: TBinding,
        systemBars: Insets
    ) = Unit

    protected open fun onViewCreated(
        binding: TBinding,
        savedInstanceState: Bundle?
    ) = Unit

    protected open fun onCreate(
        binding: TBinding,
        savedInstanceState: Bundle?
    ) = Unit

    protected open fun onCreateView(
        binding: TBinding,
        savedInstanceState: Bundle?
    ) = Unit

    final override fun onCreateView(
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? = super.onCreateView(
        name,
        context,
        attrs
    )

    final override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? = super.onCreateView(
        parent,
        name,
        context,
        attrs
    )
}