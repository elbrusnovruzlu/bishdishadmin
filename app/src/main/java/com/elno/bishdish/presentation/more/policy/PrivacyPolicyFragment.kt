package com.elno.bishdish.presentation.more.policy

import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.elno.bishdish.common.Resource
import com.elno.bishdish.databinding.FragmentPrivacyPolicyBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>(FragmentPrivacyPolicyBinding::inflate) {

    private val viewModel: PrivacyPolicyViewModel by viewModels()

    override fun setupViews() {
        viewModel.getPolicy()
    }

    override fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun setupObservers() {
        viewModel.policyResult.observe(
            viewLifecycleOwner,
            ::consumePolicyResult
        )
    }

    private fun consumePolicyResult(resource: Resource<String?>?) {
        when(resource) {
            is  Resource.Loading -> {
                binding.loading.isVisible = true
            }
            is  Resource.Success -> {
                binding.loading.isVisible = false
                resource.data?.let { binding.description.text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)  }
            }
            is  Resource.Error -> {
                binding.loading.isVisible = false
                Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

}