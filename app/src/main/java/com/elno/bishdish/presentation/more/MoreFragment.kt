package com.elno.bishdish.presentation.more

import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.elno.bishdish.R
import com.elno.bishdish.databinding.FragmentMoreBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>(FragmentMoreBinding::inflate) {

    override fun setupViews() {
        binding.contactUs.icon.setImageResource(R.drawable.ic_contact_us)
        binding.privacyPolicy.icon.setImageResource(R.drawable.ic_privacy)
        binding.language.icon.setImageResource(R.drawable.ic_laguage)
        binding.appIcon.icon.setImageResource(R.drawable.ic_sticker_circle)
        binding.appearance.icon.setImageResource(R.drawable.ic_appereance)

        context?.let {
            binding.contactUs.icon.setColorFilter(ContextCompat.getColor(it, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.privacyPolicy.icon.setColorFilter(ContextCompat.getColor(it, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.language.icon.setColorFilter(ContextCompat.getColor(it, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.appIcon.icon.setColorFilter(ContextCompat.getColor(it, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
            binding.appearance.icon.setColorFilter(ContextCompat.getColor(it, R.color.brandColor), android.graphics.PorterDuff.Mode.SRC_IN)
        }



        binding.contactUs.title.text = getString(R.string.vendor_contact)
        binding.privacyPolicy.title.text = getString(R.string.privacy_policy)
        binding.language.title.text = getString(R.string.app_language)
        binding.appIcon.title.text = getString(R.string.app_icon)
        binding.appearance.title.text = getString(R.string.appearance)
    }

    override fun setupListeners() {
        binding.contactUs.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_contactUsFragment)
        }
        binding.privacyPolicy.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_privacyPolicyFragment)
        }
        binding.language.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_languageFragment)
        }
        binding.appIcon.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_appIconFragment)
        }
        binding.appearance.root.setOnClickListener {
            findNavController().navigate(R.id.action_moreFragment_to_appearanceFragment)
        }
    }

}