package com.sample.application.ea.ui.main.generic;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.sample.application.ea.activity.main.MainActivity;
import com.sample.application.ea.ui.generic.NavigationFragment;

/**
 * Abstract base class for fragments with main activity bindings.
 */
public abstract class MainBindingFragment<TBinding extends ViewDataBinding>
        extends NavigationFragment<TBinding, MainActivity> {

    public MainBindingFragment() {
        super();
    }

    public MainBindingFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Returns the FirebaseDatabase instance.
     */
    @NonNull
    final protected FirebaseDatabase firebaseDatabase() {
        return requireParentActivity().firebaseDatabase();
    }

    /**
     * Returns the FirebaseAuth instance.
     */
    @NonNull
    final protected FirebaseAuth firebaseAuth() {
        return requireParentActivity().firebaseAuth();
    }

    /**
     * Returns the current FirebaseUser, if any.
     */
    @Nullable
    final protected FirebaseUser currentUser() {
        return firebaseAuth().getCurrentUser();
    }

    /**
     * Navigates up in the navigation stack.
     */
    final protected void navigateUp() {
        requireParentActivity().navigateUp();
    }

    /**
     * Navigates to a specified destination by ID.
     */
    final protected void navigate(@IdRes int id) {
        requireParentActivity().navigate(id, null);
    }

    /**
     * Navigates to a specified destination with arguments.
     */
    final protected void navigate(@IdRes int id, @NonNull Bundle args) {
        requireParentActivity().navigate(id, args);
    }
}
