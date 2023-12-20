package com.elno.bishdishadmin.presentation.dashboard

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.elno.bishdish.databinding.FragmentDashboardBinding
import com.elno.bishdishadmin.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate) {



    private val viewModel: DashboardViewModel by viewModels()

    override fun setupViews() {
    }

    override fun setupListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
        )
    }

    override fun setupObservers() {

    }


}