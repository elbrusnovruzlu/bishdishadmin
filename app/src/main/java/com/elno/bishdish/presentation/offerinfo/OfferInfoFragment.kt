package com.elno.bishdish.presentation.offerinfo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateMargins
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.elno.bishdish.R
import com.elno.bishdish.common.Constants.FAVOURITE_LIST
import com.elno.bishdish.common.Constants.OFFER_MODEL
import com.elno.bishdish.common.UtilityFunctions
import com.elno.bishdish.common.UtilityFunctions.dpToPx
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.common.UtilityFunctions.getType
import com.elno.bishdish.data.local.LocalDataStore
import com.elno.bishdish.databinding.FragmentOfferInfoBinding
import com.elno.bishdish.domain.model.VendorModel
import com.elno.bishdish.presentation.adapter.OfferInfoAdapter
import com.elno.bishdish.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfferInfoFragment : BaseFragment<FragmentOfferInfoBinding>(FragmentOfferInfoBinding::inflate) {

    var vendorModel: VendorModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorModel= arguments?.getParcelable(OFFER_MODEL)
    }


    override fun setupViews() {
        setButtonMargin()
        vendorModel?.imageUrl?.let {
            Glide.with(requireContext())
                .load(it)
                .into(binding.imageView)
        }
        binding.favButton.isChecked = LocalDataStore(context).getList<String>(FAVOURITE_LIST).contains(vendorModel?.id) == true
        binding.name.text = getLocalizedTextFromMap(context, vendorModel?.title)
        binding.type.text = vendorModel?.type?.map { getType(context, it) }.toString()

        binding.viewPager.isUserInputEnabled = false
        binding.viewPager.adapter = OfferInfoAdapter(this, vendorModel)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.vendor_detail)
                1 -> getString(R.string.vendor_gallery)
                else -> getString(R.string.vendor_contact)
            }
        }.attach()

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab: View = ((binding.tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)) as View
            val p = tab.layoutParams as MarginLayoutParams
            p.setMargins(0, 0, dpToPx(requireContext(), 8), 0)
            tab.requestLayout()
        }

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.callButton.isVisible = tab.position == 0
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun setButtonMargin() {
        if (isEdgeToEdgeEnabled() != 2) {
            val layoutParams = binding.callButton.layoutParams as MarginLayoutParams
            layoutParams.updateMargins(
                bottom = dpToPx(requireContext(), 64)
            )
        }
    }
    override fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.favButton.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                LocalDataStore(context).addToList(vendorModel?.id, FAVOURITE_LIST)
            }
            else {
                LocalDataStore(context).removeFromList(vendorModel?.id, FAVOURITE_LIST)
            }
        }
        binding.callButton.setOnClickListener {
            dialPhoneNumber()
        }
    }

    private fun dialPhoneNumber() {
//        vendorModel?.mobile?.let { mobile ->
//            val dialIntent = Intent(Intent.ACTION_DIAL)
//            dialIntent.data = Uri.parse("tel:${mobile}")
//            startActivity(dialIntent)
//        }
    }

    private fun isEdgeToEdgeEnabled(): Int {
        val resourceId = resources.getIdentifier("config_navBarInteractionMode", "integer", "android")
        return if (resourceId > 0) {
            resources.getInteger(resourceId)
        } else 0
    }

}