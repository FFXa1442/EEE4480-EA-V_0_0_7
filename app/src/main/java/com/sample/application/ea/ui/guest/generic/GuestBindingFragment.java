package com.sample.application.ea.ui.guest.generic;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.sample.application.ea.activity.guest.GuestActivity;
import com.sample.application.ea.ui.generic.ViewDataBindingFragment;

/**
 * Abstract base class for fragments that bind to a guest activity.
 * This class provides utility methods to access Firebase services
 * and navigation controls within the parent activity.
 *
 * @param <TBinding> Type parameter extending ViewDataBinding.
 */
public abstract class GuestBindingFragment<TBinding extends ViewDataBinding>
        extends ViewDataBindingFragment<TBinding, GuestActivity> {

    public GuestBindingFragment() {
        super();
    }

    public GuestBindingFragment(int contentLayoutId) {
        super(contentLayoutId);
    }

    /**
     * Retrieves the FirebaseDatabase instance from the parent activity.
     *
     * @return FirebaseDatabase instance.
     */
    @NonNull
    final protected FirebaseDatabase firebaseDatabase() {
        return requireParentActivity().firebaseDatabase();
    }

    /**
     * Retrieves the FirebaseAuth instance from the parent activity.
     *
     * @return FirebaseAuth instance.
     */
    @NonNull
    final protected FirebaseAuth firebaseAuth() {
        return requireParentActivity().firebaseAuth();
    }

    /**
     * Retrieves the current FirebaseUser, if logged in.
     *
     * @return Current FirebaseUser or null if not logged in.
     */
    @Nullable
    final protected FirebaseUser currentUser() {
        return firebaseAuth().getCurrentUser();
    }

    /**
     * Navigates up in the navigation hierarchy.
     */
    final protected void navigateUp() {
        requireParentActivity().navigateUp();
    }

    /**
     * Navigates to a specified destination using a resource ID.
     *
     * @param id Resource ID of the destination.
     */
    final protected void navigate(@IdRes int id) {
        requireParentActivity().navigate(id, null);
    }

    /**
     * Navigates to a specified destination using a resource ID and arguments.
     *
     * @param id   Resource ID of the destination.
     * @param args Bundle of arguments to pass to the destination.
     */
    final protected void navigate(@IdRes int id, @NonNull Bundle args) {
        requireParentActivity().navigate(id, args);
    }

}