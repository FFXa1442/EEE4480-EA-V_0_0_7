package com.sample.application.ea.ui.generic

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class ViewDataBindingFragment<TBinding, TParentActivity> :
    Fragment where TBinding : ViewDataBinding,
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
        context: Context
    ) = super.onAttach(context).let {
        (context as TParentActivity).let {
            parentActivity = it
        }
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

    protected open fun onCreateView(
        binding: TBinding,
        savedInstanceState: Bundle?
    ) = Unit

    final override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) = onViewCreated(binding, savedInstanceState)

    protected open fun onViewCreated(
        binding: TBinding,
        savedInstanceState: Bundle?
    ) = Unit

}