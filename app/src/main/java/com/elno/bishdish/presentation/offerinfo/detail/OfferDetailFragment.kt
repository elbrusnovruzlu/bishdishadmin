package com.elno.bishdish.presentation.offerinfo.detail

import android.os.Bundle
import androidx.core.text.HtmlCompat
import com.elno.bishdish.common.Constants
import com.elno.bishdish.common.Static
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.databinding.FragmentOfferDetailBinding
import com.elno.bishdish.domain.model.VendorModel
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OfferDetailFragment : BaseFragment<FragmentOfferDetailBinding>(FragmentOfferDetailBinding::inflate) {

    var vendorModel: VendorModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vendorModel= arguments?.getParcelable(Constants.OFFER_MODEL)
    }

    override fun setupViews() {
        var text = ""
        vendorModel?.ingredients?.forEach { type ->
            text += getLocalizedTextFromMap(requireContext(), Static.filterModel.ingredient?.find { it?.type == type }?.name) + "\n"
        }
        binding.description.text = text
    }

}