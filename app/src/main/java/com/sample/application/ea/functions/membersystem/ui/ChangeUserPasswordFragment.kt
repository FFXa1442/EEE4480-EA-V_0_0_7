package com.sample.application.ea.functions.membersystem.ui

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentChangeUserPasswordBinding
import com.sample.application.ea.functions.PasswordValidation.patternMatches
import com.sample.application.ea.functions.membersystem.constant.MemberSystem.PASSWORD_INVALID_MESSAGE
import com.sample.application.ea.text.doOnTextChanged

class ChangeUserPasswordFragment :
    MemberSystemBindingFragment<FragmentChangeUserPasswordBinding>() {
    override val layoutId: Int =
        R.layout.fragment_change_user_password

    override fun onCreateView(
        binding: FragmentChangeUserPasswordBinding,
        savedInstanceState: Bundle?,
    ) {
        binding.submitButton.apply {
            setEnabled(false)
            setOnClickListener {
                onSubmitButtonClick(
                    binding = binding,
                    parentActivity = parentActivity,
                    user = currentUser!!
                )
            }
        }
        binding.cancelButton.setOnClickListener {
            parentActivity.finish()
        }
        binding.userCurrentPasswordEditText.doOnTextChanged { s ->
            val editText = binding.userNewPasswordEditText
            val other = editText.text.toString().trim()
            binding.submitButton.isEnabled = s!!.isNotEmpty() || other.isNotEmpty()
        }
        binding.userNewPasswordEditText.doOnTextChanged { s ->
            if (s!!.isEmpty() || patternMatches(s.toString())) {
                binding.submitButton.isEnabled = true
                binding.userNewPasswordTextLayout.error = null
            } else {
                binding.submitButton.isEnabled = false
                binding.userNewPasswordTextLayout.error = PASSWORD_INVALID_MESSAGE
            }
        }
    }

}