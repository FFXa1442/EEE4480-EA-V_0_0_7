package com.sample.application.ea.ui.guest.start;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sample.application.ea.R;
import com.sample.application.ea.databinding.FragmentStartBinding;

/**
 * Final class representing the Start Fragment.
 */
public final class StartFragment extends StartFragmentBase {
}

/**
 * Abstract base class for the Start Fragment.
 */
abstract class StartFragmentBase extends GuestBindingFragment<FragmentStartBinding> {

    public StartFragmentBase() {
        super();
    }

    public StartFragmentBase(int contentLayoutId) {
        super(contentLayoutId);
    }

    // Override for view creation in the Start Fragment.
    @Override
    protected void onCreateView(@NonNull FragmentStartBinding binding, @Nullable Bundle container) {
        // Set up start button listener
        binding.startButton.setOnClickListener(v -> navigate(R.id.action_nav_start_to_nav_sign_in));
    }

    /**
     * Returns the layout resource ID for the Start Fragment.
     *
     * @return Layout resource ID.
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_start;
    }
}