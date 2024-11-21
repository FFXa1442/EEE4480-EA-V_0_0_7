package com.sample.application.ea.ui.main.generic;

import androidx.databinding.ViewDataBinding;

import com.sample.application.ea.activity.main.MainActivity;
import com.sample.application.ea.ui.generic.ViewDataBindingDialogFragment;

/**
 * Abstract base class for dialog fragments with main activity bindings.
 */
public abstract class MainBindingSheetDialogFragment<TBinding extends ViewDataBinding>
        extends ViewDataBindingDialogFragment<TBinding, MainActivity> {

    public MainBindingSheetDialogFragment() {
        super();
    }

    public MainBindingSheetDialogFragment(int contentLayoutId) {
        super(contentLayoutId);
    }
}
