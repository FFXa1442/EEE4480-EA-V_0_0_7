package com.sample.application.ea.functions.membersystem.ui

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.sample.application.ea.databinding.FragmentChangeUserNameBinding
import com.sample.application.ea.functions.membersystem.activity.MemberActivity
import com.sample.application.ea.functions.membersystem.constant.MemberSystem
import com.sample.application.ea.widget.LoadingDialog

fun ChangeUserNameFragment.onSubmitButtonClick(
    binding: FragmentChangeUserNameBinding,
    parentActivity: MemberActivity,
    user: FirebaseUser,
) = View.OnClickListener {

    val newName = binding.userNameEditText.text.toString().trim()

    when {
        newName.isEmpty() -> {
            binding.userNameTextLayout.error = "User name is required."
            return@OnClickListener
        }
    }

    val dialog = LoadingDialog().apply {
        show(childFragmentManager, "Loading Dialog")
    }

    user.updateProfile(UserProfileChangeRequest.Builder().apply {
        displayName = newName
        photoUri = user.photoUrl
    }.build()).addOnCompleteListener {
        parentActivity.setResult(RESULT_OK, Intent().apply {
            putExtra(MemberSystem.SUBMIT_RESULT, newName)
        })
        dialog.dismiss()
        parentActivity.finish()
    }

}