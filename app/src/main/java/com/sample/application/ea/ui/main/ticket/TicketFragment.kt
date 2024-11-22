package com.sample.application.ea.ui.main.ticket

import android.os.Bundle
import com.sample.application.ea.R
import com.sample.application.ea.databinding.FragmentTicketBinding
import com.sample.application.ea.ui.main.generic.MainBindingFragment

class TicketFragment : MainBindingFragment<FragmentTicketBinding>() {

    override val layoutId: Int = R.layout.fragment_ticket

    override fun onCreateView(
        binding: FragmentTicketBinding,
        savedInstanceState: Bundle?,
    ) {
        binding.japanButton.setOnClickListener(onClickListener)
        binding.southKoreaButton.setOnClickListener(onClickListener)
        binding.taiwanButton.setOnClickListener(onClickListener)
        binding.britainButton.setOnClickListener(onClickListener)
        binding.chinaButton.setOnClickListener(onClickListener)
        binding.singaporeButton.setOnClickListener(onClickListener)
        binding.malaysiaButton.setOnClickListener(onClickListener)
        binding.unitedStateButton.setOnClickListener(onClickListener)
    }

}