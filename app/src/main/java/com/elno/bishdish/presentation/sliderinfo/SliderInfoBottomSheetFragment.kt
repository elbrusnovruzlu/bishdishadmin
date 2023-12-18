package com.elno.bishdish.presentation.sliderinfo

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.elno.bishdish.common.UtilityFunctions.getLocalizedTextFromMap
import com.elno.bishdish.databinding.FragmentSliderInfoBottomSheetBinding
import com.elno.bishdish.domain.model.SliderModel
import com.elno.bishdish.presentation.base.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SliderInfoBottomSheetFragment(private val sliderModel: SliderModel?) : BaseDialogFragment<FragmentSliderInfoBottomSheetBinding>(FragmentSliderInfoBottomSheetBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rootView.setOnClickListener {
            dismiss()
        }

        context?.let { ctx ->
            sliderModel?.let { model ->
                Glide.with(ctx)
                    .load(getLocalizedTextFromMap(ctx, model.image))
                    .into(binding.image)
                binding.title.text = getLocalizedTextFromMap(ctx, model.title)
                binding.description.text = getLocalizedTextFromMap(ctx, model.description)
            }
        }
    }
}