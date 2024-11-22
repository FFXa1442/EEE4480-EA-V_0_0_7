package com.sample.application.ea.ui.guest.signin

import androidx.fragment.app.FragmentManager
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.sample.application.ea.widget.LoadingDialog
import java.lang.Exception

class SignInHelper private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val fragmentManager: FragmentManager,
    private var email: String = "",
    private var password: String = "",
    private var onSuccessListener: (AuthResult) -> Unit = {},
    private var onFailureListener: (Exception) -> Unit = {},
    private var onCompleteListener: (Task<AuthResult>) -> Unit = {},
) {

    companion object {
        @JvmStatic
        fun newInstance(
            auth: FirebaseAuth,
            fragmentManager: FragmentManager,
        ) = SignInHelper(
            firebaseAuth = auth,
            fragmentManager = fragmentManager
        )
    }

    fun setUserEmailEditText(
        editText: TextInputEditText,
    ): SignInHelper {
        email = editText.text.toString().trim()
        return this
    }

    fun setUserPasswordEditText(
        editText: TextInputEditText,
    ): SignInHelper {
        password = editText.text.toString().trim()
        return this
    }

    fun setOnSuccessListener(
        action: (AuthResult) -> Unit,
    ): SignInHelper {
        onSuccessListener = action
        return this
    }

    fun setOnCompleteListener(
        action: (Task<AuthResult>) -> Unit,
    ): SignInHelper {
        onCompleteListener = action
        return this
    }

    fun setOnFailureListener(
        action: (Exception) -> Unit,
    ): SignInHelper {
        onFailureListener = action
        return this
    }

    fun signIn() {
        val dialog = LoadingDialog().apply {
            show(
                this@SignInHelper.fragmentManager,
                "Loading Dialog"
            )
        }
        firebaseAuth.signOut()
        assert(firebaseAuth.currentUser == null) { "'user' must be null!!" }
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccessListener(it) }
            .addOnFailureListener { onFailureListener(it) }
            .addOnCompleteListener {
                dialog.dismiss()
                onCompleteListener(it)
            }

    }

}