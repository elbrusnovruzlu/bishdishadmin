package com.elno.bishdish.presentation.more

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.navigation.fragment.findNavController
import com.elno.bishdish.BuildConfig
import com.elno.bishdish.R
import com.elno.bishdish.databinding.FragmentAppIconBinding
import com.elno.bishdish.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppIconFragment : BaseFragment<FragmentAppIconBinding>(FragmentAppIconBinding::inflate) {

    override fun setupViews() {
        val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        val checkButton = when(sharedPref?.getString("appIcon", AppIconType.DEFAULT.name) ?: AppIconType.DEFAULT.name) {
            AppIconType.DEFAULT.name -> R.id.dayzDefault
            AppIconType.BLACK.name -> R.id.dayzBlack
            AppIconType.WHITE.name-> R.id.dayzWhite
            else -> R.id.dayzDefault
        }
        binding.radioButton.check(checkButton)
    }

    override fun setupListeners() {
        binding.radioButton.setOnCheckedChangeListener { _, checkedId ->
            val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
            val selectedAppIcon: String = sharedPref?.getString("appIcon", AppIconType.DEFAULT.name) ?: AppIconType.DEFAULT.name
            when(checkedId) {
                R.id.dayzDefault-> if (selectedAppIcon != AppIconType.DEFAULT.name) {
                    setIcon(AppIconType.DEFAULT)
                }
                R.id.dayzBlack -> if (selectedAppIcon != AppIconType.BLACK.name) {
                    setIcon(AppIconType.BLACK)
                }
                R.id.dayzWhite -> if (selectedAppIcon != AppIconType.WHITE.name) {
                    setIcon(AppIconType.WHITE)
                }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    enum class AppIconType { DEFAULT, BLACK, WHITE }

    private fun setIcon(targetColour: AppIconType) {
        val sharedPref = activity?.getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString("appIcon", targetColour.name)?.apply()
        for (value in AppIconType.values()) {
            val action = if (value == targetColour) {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            } else {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
            activity?.packageManager?.setComponentEnabledSetting(
                ComponentName(BuildConfig.APPLICATION_ID, "${BuildConfig.APPLICATION_ID}.${value.name}"),
                action, PackageManager.DONT_KILL_APP
            )
        }
    }

}