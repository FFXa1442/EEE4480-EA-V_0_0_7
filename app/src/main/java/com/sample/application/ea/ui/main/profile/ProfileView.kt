package com.sample.application.ea.ui.main.profile

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.navigation.NavigationView
import com.sample.application.ea.databinding.FragmentProfileBinding

class ProfileView : NavigationView {

    private val binding: FragmentProfileBinding

    @Deprecated("")
    constructor(
        context: Context,
    ) : super(context) {
        binding = inflateLayout(context)
    }

    @Deprecated("")
    constructor(
        context: Context,
        attrs: AttributeSet?,
    ) : super(context, attrs) {
        binding = inflateLayout(context)
    }

    @Deprecated("")
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        binding = inflateLayout(context)
    }

    private fun inflateLayout(
        context: Context,
    ) = FragmentProfileBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    fun setOnSignOutButtonClick(listener: OnClickListener) {
        binding.signOutButton.setOnClickListener(listener)
    }

    fun setOnChangePasswordButtonClick(listener: OnClickListener) {
        binding.changePasswordButton.setOnClickListener(listener)
    }

    fun setOnChangeNameButtonClick(listener: OnClickListener) {
        binding.changeNameButton.setOnClickListener(listener)
    }

    fun setOnUserIconButtonClick(listener: OnClickListener) {
        binding.userIcon.setOnClickListener(listener)
    }

    fun setUserIconDrawable(drawable: Drawable) {
        binding.userIcon.setImageDrawable(drawable)
    }

    fun setUserEmailText(text: String) {
        binding.userEmail.text = text
    }

    fun setUserNameText(text: String) {
        binding.userName.text = text
    }

}