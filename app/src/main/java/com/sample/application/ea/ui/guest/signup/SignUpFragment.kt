package com.sample.application.ea.ui.guest.signup

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentSignUpBinding
import com.sample.application.ea.functions.EmailValidation
import com.sample.application.ea.functions.PasswordValidation
import com.sample.application.ea.functions.membersystem.constant.MemberSystem.PASSWORD_INVALID_MESSAGE
import com.sample.application.ea.text.doOnTextChanged
import com.sample.application.ea.ui.guest.generic.GuestBindingFragment

class SignUpFragment : GuestBindingFragment<FragmentSignUpBinding>() {

    override val layoutId: Int = R.layout.fragment_sign_up

    private val checker: Checker by lazy {
        Checker()
    }

    fun resetEditBox() {
        binding.userNameEditText.setText("")
        binding.userEmailEditText.setText("")
        binding.userPasswordEditText.setText("")
    }

    override fun onCreateView(
        binding: FragmentSignUpBinding,
        savedInstanceState: Bundle?,
    ) {
        binding.registerButton.apply {
            isEnabled = false
            setOnClickListener {
                val name = binding.userNameEditText.text.toString().trim()
                when {
                    name.isEmpty() -> {
                        binding.userNameTextLayout.error = "User name is required."
                        return@setOnClickListener
                    }

                    else -> {
                        val email = binding.userEmailEditText.text.toString()
                        val password = binding.userPasswordEditText.text.toString()

                        val emailIsValid = EmailValidation.patternMatches(email)
                        when {
                            !EmailValidation.patternMatches(email) -> {
                                binding.userEmailTextLayout.error = "Invalid email address"
                            }
                        }

                        val passwordIsValid = PasswordValidation.patternMatches(password)
                        when {
                            !passwordIsValid -> {
                                binding.userPasswordTextLayout.error = "Invalid password"
                            }
                        }

                        when {
                            emailIsValid && passwordIsValid -> {
                                signUp(
                                    name = name,
                                    email = email,
                                    password = password,
                                    firebaseAuth = firebaseAuth
                                )
                            }

                            else -> {
                                resetEditBox()
                            }
                        }
                    }
                }
            }
        }

        binding.backButton.setOnClickListener {
            navigateUp()
        }

        binding.userNameEditText.doOnTextChanged { s ->
            binding.userNameTextLayout.error = null
            checker.updateNameState(s!!.isNotEmpty())
        }

        binding.userEmailEditText.doOnTextChanged { s ->
            when {
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

    private inner class Checker(
        private var nameIsValid: Boolean = false,
        private var emailIsValid: Boolean = false,
        private var passwordIsValid: Boolean = false,
    ) {
        fun updateNameState(flag: Boolean) {
            this.nameIsValid = flag
            update()
        }

        fun updateEmailState(flag: Boolean) {
            this.emailIsValid = flag
            update()
        }

        fun updatePasswordState(flag: Boolean) {
            this.passwordIsValid = flag
            update()
        }

        private fun update() {
            binding.registerButton.isEnabled =
                nameIsValid && emailIsValid && passwordIsValid
        }
    }
}