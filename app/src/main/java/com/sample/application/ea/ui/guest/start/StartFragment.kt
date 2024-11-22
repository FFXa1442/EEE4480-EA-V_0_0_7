package com.sample.application.ea.ui.guest.start

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentStartBinding
import com.sample.application.ea.ui.guest.generic.GuestBindingFragment

class StartFragment :
    GuestBindingFragment<FragmentStartBinding>() {
    override val layoutId: Int = R.layout.fragment_start

    override fun onCreateView(
        binding: FragmentStartBinding,
        savedInstanceState: Bundle?,
    ) = binding.startButton.setOnClickListener {
        navigate(R.id.action_nav_start_to_nav_sign_in)
    }

}