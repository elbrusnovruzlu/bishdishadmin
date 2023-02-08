package com.elno.wedding.presentation.more

import androidx.navigation.fragment.findNavController
import com.elno.wedding.R
import com.elno.wedding.databinding.FragmentMoreBinding
import com.elno.wedding.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>(FragmentMoreBinding::inflate) {

    override fun setupViews() {
        binding.contactUs.icon.setImageResource(R.drawable.ic_contact_us)
        binding.privacyPolicy.icon.setImageResource(R.drawable.ic_privacy)
        binding.language.icon.setImageResource(R.drawable.ic_laguage)

        binding.contactUs.title.text = getString(R.string.contact_us)
        binding.privacyPolicy.title.text = getString(R.string.privacy_policy)
        binding.language.title.text = getString(R.string.app_language)
    }

    override fun setupListeners() {
        binding.contactUs.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_contactUsFragment)
        }
        binding.privacyPolicy.root.setOnClickListener {
            val dialog = PrivacyPolicyBottomSheetFragment(
                getString(R.string.privacy_policy),
                getString(R.string.privacy_pocily)
            )
            dialog.show(parentFragmentManager, PrivacyPolicyBottomSheetFragment::class.java.canonicalName)
        }
        binding.language.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_languageFragment)
        }
    }

}