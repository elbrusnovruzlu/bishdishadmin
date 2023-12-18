package com.elno.bishdish.presentation.more

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.findNavController
import com.elno.bishdish.MainActivity
import com.elno.bishdish.R
import com.elno.bishdish.databinding.FragmentAppreanceBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppearanceFragment : BaseFragment<FragmentAppreanceBinding>(FragmentAppreanceBinding::inflate) {

    override fun setupViews() {
        val checkButton = if(checkIfNightModeSet()) {
            when((activity as? MainActivity)?.checkIfNightMode()) {
                true -> R.id.darkMode
                else -> R.id.lightMode
            }
        }
        else {
           R.id.defaultMode
        }

        binding.radioButton.check(checkButton)
    }

    override fun setupListeners() {
        binding.radioButton.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId) {
                R.id.darkMode-> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.putBoolean("isDarkModeActive", true)?.apply()
                    (activity as MainActivity).navigateTo(R.id.dashboardFragment)
                }
                R.id.lightMode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.putBoolean("isDarkModeActive", false)?.apply()
                    (activity as MainActivity).navigateTo(R.id.dashboardFragment)
                }
                R.id.defaultMode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
                    sharedPref?.edit()?.remove("isDarkModeActive")?.apply()
                    (activity as MainActivity).navigateTo(R.id.dashboardFragment)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun checkIfNightModeSet(): Boolean {
        val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        return sharedPref?.contains("isDarkModeActive") == true
    }

}