package com.sample.application.ea.ui.main.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.sample.application.ea.databinding.FragmentProfileBinding;
import com.sample.application.ea.utilities.TryUtil;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ProfileView class that extends ProfileViewSetup.
 * Manages user profile UI components.
 */
public final class ProfileView extends ProfileViewSetup {

    @Deprecated
    public ProfileView(@NonNull Context context) {
        super(context);
    }

    @Deprecated
    public ProfileView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Deprecated
    public ProfileView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

/**
 * Abstract class ProfileViewSetup.
 * Sets up profile view components and interactions.
 */
abstract class ProfileViewSetup extends ProfileViewBase {

    // Binding for the profile layout
    protected final FragmentProfileBinding binding;

    public ProfileViewSetup(@NonNull Context context) {
        super(context);
        binding = inflateLayout(context);
    }

    public ProfileViewSetup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = inflateLayout(context);
    }

    public ProfileViewSetup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = inflateLayout(context);
    }

    /**
     * Inflates the profile layout.
     * @param context the application context
     * @return the inflated FragmentProfileBinding
     */
    private FragmentProfileBinding inflateLayout(@NonNull Context context) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final FragmentProfileBinding binding
                = FragmentProfileBinding.inflate(inflater, this, true);
        return bindSetup(binding);
    }

    /**
     * Sets up the binding for the profile view.
     * @param binding the FragmentProfileBinding
     * @return the initialized binding
     */
    private FragmentProfileBinding bindSetup(@NonNull FragmentProfileBinding binding) {
        return binding;
    }

    /**
     * Sets the click listener for the sign-out button.
     * @param listener the click listener
     */
    final public void setOnSignOutButtonClick(@NonNull View.OnClickListener listener) {
        binding.signOutButton.setOnClickListener(listener);
    }

    /**
     * Sets the click listener for the change password button.
     * @param listener the click listener
     */
    final public void setOnChangePasswordButtonClick(@NonNull View.OnClickListener listener) {
        binding.changePasswordButton.setOnClickListener(listener);
    }

    /**
     * Sets the click listener for the change name button.
     * @param listener the click listener
     */
    final public void setOnChangeNameButtonClick(@NonNull View.OnClickListener listener) {
        binding.changeNameButton.setOnClickListener(listener);
    }

    /**
     * Sets the click listener for the user icon button.
     * @param listener the click listener
     */
    final public void setOnUserIconButtonClick(@NonNull View.OnClickListener listener) {
        binding.userIcon.setOnClickListener(listener);
    }

    /**
     * Sets the drawable for the user icon.
     * @param drawable the drawable to set
     */
    final public void setUserIconDrawable(@NonNull Drawable drawable) {
        binding.userIcon.setImageDrawable(drawable);
    }

    /**
     * Sets the text for the user email.
     * @param text the email text
     */
    final public void setUserEmailText(@NonNull String text) {
        binding.userEmail.setText(text);
    }

    /**
     * Sets the text for the user name.
     * @param text the name text
     */
    final public void setUserNameText(@NonNull String text) {
        binding.userName.setText(text);
    }
}

/**
 * Abstract base class ProfileViewBase.
 * Extends ProfileViewInterfaceBase.
 */
abstract class ProfileViewBase extends NavigationView {

    public ProfileViewBase(@NonNull Context context) {
        super(context);
    }

    public ProfileViewBase(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileViewBase(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}