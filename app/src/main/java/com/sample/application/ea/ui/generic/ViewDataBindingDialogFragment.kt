package com.sample.application.ea.ui.generic

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.internal.EdgeToEdgeUtils

abstract class ViewDataBindingDialogFragment<TBinding, TParentActivity> :
    BottomSheetDialogFragment where TBinding : ViewDataBinding,
                                    TParentActivity : AppCompatActivity {

    protected lateinit var binding: TBinding

    protected lateinit var parentActivity: TParentActivity

    @get:LayoutRes
    protected abstract val layoutId: Int

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    @Suppress("UNCHECKED_CAST")
    @CallSuper
    override fun onAttach(
        context: Context,
    ) = super.onAttach(context).let {
        (context as TParentActivity).let {
            parentActivity = it
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = DataBindingUtil.inflate<TBinding>(
        inflater,
        layoutId,
        container,
        false
    ).also { dataBinding ->
        binding = dataBinding
        viewLifecycleOwner.also { lifecycleOwner ->
            dataBinding.lifecycleOwner = lifecycleOwner
        }
        onCreateView(
            dataBinding,
            savedInstanceState
        )
    }.root

    protected open fun onApplyWindowInsets(
        view: View,
        binding: TBinding,
        systemBars: Insets,
    ) = Unit

    protected open fun onCreateView(
        binding: TBinding,
        savedInstanceState: Bundle?,
    ) = Unit

    final override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        onViewCreated(binding, savedInstanceState)
    }

    protected open fun onViewCreated(
        binding: TBinding,
        savedInstanceState: Bundle?,
    ) = Unit

    protected open val enableFullScreen = false

    @SuppressLint("RestrictedApi")
    @CallSuper
    override fun onCreateDialog(savedInstanceState: Bundle?) =
        super.onCreateDialog(savedInstanceState).also { dialog ->
            dialog.apply {

                dialog.window!!.decorView.let { view ->
                    ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

                        this@ViewDataBindingDialogFragment.onApplyWindowInsets(
                            v,
                            binding,
                            insets.getInsets(
                                WindowInsetsCompat.Type.systemBars()
                            )
                        )
                        insets
                    }
                }


                if (enableFullScreen) {
                    window!!.apply {
                        setFlags(
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        )
                    }.also { window ->
                        if (VERSION.SDK_INT >= VERSION_CODES.VANILLA_ICE_CREAM)
                            EdgeToEdgeUtils.applyEdgeToEdge(window, true)
                    }
                }
                setOnShowListener { dialogInterface ->
                    (dialogInterface as BottomSheetDialog)
                        .also(::setupBehavior)
                }
            }
        }

    private fun setupBehavior(bottomSheetDialog: BottomSheetDialog) =
        BottomSheetBehavior.from(
            bottomSheetDialog.requireViewById<FrameLayout>(
                R.id.design_bottom_sheet
            )
        ).let { behaviour ->
            behaviour.skipCollapsed = true
            behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            behaviour.setDraggable(false)
        }

}