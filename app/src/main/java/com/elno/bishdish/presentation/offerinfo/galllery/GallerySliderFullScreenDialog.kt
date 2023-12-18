package com.elno.bishdish.presentation.offerinfo.galllery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.elno.bishdish.R
import com.elno.bishdish.databinding.DialogGallerySliderFullscreenBinding
import com.elno.bishdish.presentation.adapter.GallerySliderAdapter


class GallerySliderFullScreenDialog(private val list: ArrayList<String>?, private val position: Int) : DialogFragment() {
    private var _binding: DialogGallerySliderFullscreenBinding? = null
    val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGallerySliderFullscreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.close.setOnClickListener {
            dismiss()
        }

        context?.let {
            val viewPagerAdapter = GallerySliderAdapter(it)
            viewPagerAdapter.submitList(list)
            binding.viewPager.adapter = viewPagerAdapter
            binding.viewPager.currentItem = position
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}