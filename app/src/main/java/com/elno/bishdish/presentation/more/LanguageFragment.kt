package com.elno.bishdish.presentation.more

import androidx.navigation.fragment.findNavController
import com.elno.bishdish.MainActivity
import com.elno.bishdish.R
import com.elno.bishdish.common.Constants
import com.elno.bishdish.common.LocaleManager
import com.elno.bishdish.databinding.FragmentLanguageBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {

    override fun setupViews() {
        val checkButton = when(LocaleManager(context).getLanguage()) {
            Constants.LANGUAGE_KEY_AZ -> R.id.az
            Constants.LANGUAGE_KEY_EN -> R.id.en
            Constants.LANGUAGE_KEY_RU -> R.id.ru
            else -> R.id.az
        }
        binding.radioButton.check(checkButton)
    }

    override fun setupListeners() {
        binding.radioButton.setOnCheckedChangeListener { group, checkedId ->
            val langString: String = LocaleManager(context).getLanguage()
            when(checkedId) {
                R.id.az-> if (langString != Constants.LANGUAGE_KEY_AZ) {
                    changeLanguage(Constants.LANGUAGE_KEY_AZ)
                }
                R.id.en -> if (langString != Constants.LANGUAGE_KEY_EN) {
                    changeLanguage(Constants.LANGUAGE_KEY_EN)
                }
                R.id.ru -> if (langString != Constants.LANGUAGE_KEY_RU) {
                    changeLanguage(Constants.LANGUAGE_KEY_RU)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun changeLanguage(id: String) {
        context?.let {
            LocaleManager(it).setNewLocale(it, id)
            activity?.recreate()
            (activity as MainActivity).navigateTo(R.id.dashboardFragment)
        }

    }

}