package com.elno.bishdish.presentation.onboard

import androidx.navigation.fragment.findNavController
import com.elno.bishdish.R
import com.elno.bishdish.databinding.FragmentOnboardBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : BaseFragment<FragmentOnboardBinding>(FragmentOnboardBinding::inflate) {

    override fun setupListeners() {
        binding.continueBtn.setOnClickListener {
            findNavController().navigate(R.id.action_onboardFragment_to_dashboardFragment)
        }
    }
}