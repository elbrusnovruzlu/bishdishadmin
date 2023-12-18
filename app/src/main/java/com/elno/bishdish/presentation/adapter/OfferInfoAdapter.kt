package com.elno.bishdish.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.elno.bishdish.common.Constants.OFFER_MODEL
import com.elno.bishdish.domain.model.VendorModel
import com.elno.bishdish.presentation.offerinfo.contact.ContactFragment
import com.elno.bishdish.presentation.offerinfo.detail.OfferDetailFragment
import com.elno.bishdish.presentation.offerinfo.galllery.OfferGalleryFragment

class OfferInfoAdapter(fragment: Fragment, private val vendorModel: VendorModel?): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> OfferDetailFragment()
            1 -> OfferGalleryFragment()
            2 -> ContactFragment()
            else -> throw IllegalStateException()
        }
        fragment.arguments = Bundle().apply {
            putParcelable(OFFER_MODEL, vendorModel)
        }

        return fragment
    }
}