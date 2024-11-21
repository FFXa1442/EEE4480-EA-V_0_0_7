package com.sample.application.ea.functions.membersystem.ui

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentChangeUserNameBinding
import com.sample.application.ea.text.doOnTextChanged

class ChangeUserNameFragment :
    MemberSystemBindingFragment<FragmentChangeUserNameBinding>() {

    override val layoutId: Int =
        R.layout.fragment_change_user_name

    override fun onCreateView(
        binding: FragmentChangeUserNameBinding,
        savedInstanceState: Bundle?,
    ) {
        binding.cancelButton.setOnClickListener {
            parentActivity.finish()
        }

        binding.submitButton.setOnClickListener {
            onSubmitButtonClick(
                binding = binding,
                parentActivity = parentActivity,
                user = currentUser!!
            )
        }

        binding.userNameEditText.doOnTextChanged {
            binding.userNameTextLayout.error = null
        }
    }
}