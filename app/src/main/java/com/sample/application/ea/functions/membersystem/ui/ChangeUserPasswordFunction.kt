package com.sample.application.ea.functions.membersystem.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.sample.application.ea.databinding.FragmentChangeUserPasswordBinding
import com.sample.application.ea.functions.PasswordValidation.patternMatches
import com.sample.application.ea.functions.membersystem.activity.MemberActivity
import com.sample.application.ea.widget.LoadingDialog

fun ChangeUserPasswordFragment.onSubmitButtonClick(
    binding: FragmentChangeUserPasswordBinding,
    parentActivity: MemberActivity,
    user: FirebaseUser,
) = View.OnClickListener {
    val oldPw = binding.userCurrentPasswordEditText.text.toString().trim()
    val newPw = binding.userNewPasswordEditText.text.toString().trim()

    when (oldPw) {
        newPw -> {
            val message = """
        The new password cannot be the same as the current password.
        Please choose a different password.
        """.trimIndent()
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(
                    "OK"
                ) { _: DialogInterface?, _: Int ->
                    resetEditBox(
                        binding = binding
                    )
                }
                .create().show()
            return@OnClickListener
        }
    }


    if (patternMatches(newPw)) {
        saveNewPassword(
            parentActivity = parentActivity,
            binding = binding,
            user = user,
            oldPw = oldPw,
            newPw = newPw,
        )
    } else {
        AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(
                "The password must be 8-20 characters long, " +
                        "include at least one digit, one uppercase letter, " +
                        "and one lowercase letter, " +
                        "and must not contain spaces."
            )
            .setCancelable(false)
            .setPositiveButton(
                "OK"
            ) { _: DialogInterface?, _: Int ->
                resetEditBox(binding = binding)
            }
            .create().show()
    }

}

private fun ChangeUserPasswordFragment.resetEditBox(
    binding: FragmentChangeUserPasswordBinding,
) {
    binding.userCurrentPasswordEditText.setText("")
    binding.userNewPasswordEditText.setText("")
}

private fun ChangeUserPasswordFragment.saveNewPassword(
    parentActivity: MemberActivity,
    binding: FragmentChangeUserPasswordBinding,
    user: FirebaseUser,
    oldPw: String,
    newPw: String,
) {
    val dialog = LoadingDialog().also {
        it.show(childFragmentManager, "Loading Dialog")
    }

    user.reauthenticate(EmailAuthProvider.getCredential(user.email!!, oldPw))
        .addOnFailureListener { e ->
            AlertDialog.Builder(requireContext())
                .setTitle("Error")
                .setMessage(e.message)
                .setCancelable(false)
                .setPositiveButton("OK") { _: DialogInterface?, _: Int ->
                    resetEditBox(
                        binding = binding
                    )
                }
                .create().show()
        }
        .addOnSuccessListener {
            user.updatePassword(newPw)
            parentActivity.finish()
        }
        .addOnCompleteListener {
            dialog.dismiss()
        }
}