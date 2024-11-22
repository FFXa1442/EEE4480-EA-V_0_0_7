package com.sample.application.ea.ui.guest.signin

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentSignInBinding
import com.sample.application.ea.functions.EmailValidation
import com.sample.application.ea.functions.PasswordValidation
import com.sample.application.ea.functions.membersystem.constant.MemberSystem.PASSWORD_INVALID_MESSAGE
import com.sample.application.ea.text.doOnTextChanged
import com.sample.application.ea.ui.guest.generic.GuestBindingFragment

class SignInFragment : GuestBindingFragment<FragmentSignInBinding>() {

    override val layoutId: Int = R.layout.fragment_sign_in

    private val checker = Checker()

    override fun onCreateView(binding: FragmentSignInBinding, savedInstanceState: Bundle?) {

        binding.signInButton.setOnClickListener {
            val email = binding.userEmailEditText.text.toString()
            val emailValid = EmailValidation.patternMatches(email)

            parentActivity.setResult(RESULT_CANCELED)

            when {
                emailValid -> {
                    SignInHelper.newInstance(
                        firebaseAuth,
                        childFragmentManager
                    ).apply {
                        setUserEmailEditText(binding.userEmailEditText)
                        setUserPasswordEditText(binding.userPasswordEditText)
                        setOnSuccessListener {
                            parentActivity.apply {
                                setResult(RESULT_OK)
                                finish()
                            }
                            setOnFailureListener { _ ->
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Error")
                                    .setMessage("The email address or password is incorrect.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK") { _, _ ->
                                        binding.userEmailEditText.setText("")
                                        binding.userPasswordEditText.setText("")
                                    }
                                    .create().show()
                            }
                        }
                        signIn()
                    }

                }

                else -> {
                    binding.userEmailTextLayout.error = "Invalid Email"
                    binding.userEmailEditText.setText("")
                }
            }

            binding.signUpButton.setOnClickListener { _ ->
                navigate(R.id.action_nav_sign_in_to_nav_sign_up)
            }

            binding.userEmailEditText.doOnTextChanged { s ->
                when {
                    s!!.isEmpty() -> {
                        checker.updateEmailState(false)
                        binding.userEmailTextLayout.error = null
                    }

                    EmailValidation.patternMatches(s.toString()) -> {
                        checker.updateEmailState(true)
                        binding.userEmailTextLayout.error = null
                    }

                    else -> {
                        checker.updateEmailState(false)
                        binding.userEmailTextLayout.error = "Invalid email address"
                    }
                }
            }

            binding.userPasswordEditText.doOnTextChanged { s ->
                when {
                    s!!.isEmpty() || PasswordValidation.patternMatches(s.toString()) -> {
                        checker.updatePasswordState(true)
                        binding.userPasswordTextLayout.error = null
                    }

                    else -> {
                        checker.updatePasswordState(false)
                        binding.userPasswordTextLayout.error = PASSWORD_INVALID_MESSAGE
                    }
                }
            }
        }

    }

    private inner class Checker(
        private var emailIsValid: Boolean = false,
        private var passwordIsValid: Boolean = false,
    ) {

        fun updateEmailState(flag: Boolean) {
            this.emailIsValid = flag
            update()
        }

        fun updatePasswordState(flag: Boolean) {
            this.passwordIsValid = flag
            update()
        }

        private fun update() {
            binding.signInButton.setEnabled(
                emailIsValid && passwordIsValid
            )
        }
    }
}