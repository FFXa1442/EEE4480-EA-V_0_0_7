package com.sample.application.ea.ui.guest.signup

import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.sample.application.ea.widget.LoadingDialog

fun SignUpFragment.signUp(
    name: String,
    email: String,
    password: String,
    firebaseAuth: FirebaseAuth,
) {

    val dialog = LoadingDialog().also {
        it.show(childFragmentManager, "Loading Dialog")
    }

    firebaseAuth.apply {
        createUserWithEmailAndPassword(email, password).apply {
            addOnFailureListener { e ->
                Log.e("SignUpBlock", e.message, e)
                resetEditBox()
                dialog.dismiss()
                AlertDialog.Builder(requireContext()).setTitle("Error")
                    .setMessage(e.message)
                    .setPositiveButton("OK") { _, _ -> resetEditBox() }
                    .create().show()
            }
            addOnSuccessListener {
                val userProfileBuilder = UserProfileChangeRequest.Builder()
                userProfileBuilder.setDisplayName(name)
                firebaseAuth.currentUser!!
                    .updateProfile(userProfileBuilder.build())
                    .addOnCompleteListener { dialog.dismiss() }
                    .addOnSuccessListener { navigateUp() }
            }
        }
    }


}