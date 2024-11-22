package com.sample.application.ea.ui.main.generic

import androidx.databinding.ViewDataBinding
import com.sample.application.ea.activity.main.MainActivity
import com.sample.application.ea.ui.generic.ViewDataBindingDialogFragment

abstract class MainBindingSheetDialogFragment<TBinding> :
    ViewDataBindingDialogFragment<TBinding, MainActivity>
        where TBinding : ViewDataBinding {
    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)
}