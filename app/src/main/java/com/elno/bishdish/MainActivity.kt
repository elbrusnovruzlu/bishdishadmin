package com.elno.bishdish

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.elno.bishdish.common.Constants.NOTIFICATION_ID
import com.elno.bishdish.common.LocaleManager
import com.elno.bishdish.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        installSplashScreen()
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val sharedPref = getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        val isOnboardShowed = sharedPref.getBoolean("isOnboardShowed", false)
        if(isOnboardShowed) {
            val graph = navController.graph
            graph.startDestination = R.id.dashboardFragment
            navController.setGraph(graph, intent.extras)
        }
        setContentView(binding.root)
        setupWithNavController(binding.bottomNavigationView, navController)
        setNightMode()
        navController.addOnDestinationChangedListener { _, destination, _ ->
             when(destination.id) {
                R.id.offerInfoFragment -> {
                    hideSystemUI()
                    binding.bottomNavigationView.isVisible = false
                }
                R.id.onboardFragment, R.id.favouriteFragment, R.id.notificationFragment, R.id.contactUsFragment, R.id.languageFragment, R.id.privacyPolicyFragment, R.id.appearanceFragment -> {
                    if(checkIfNightMode()) showSystemUIOnNightMode() else showSystemUI()
                    binding.bottomNavigationView.isVisible = false
                }
                else -> {
                    if(checkIfNightMode()) showSystemUIOnNightMode() else showSystemUI()
                    binding.bottomNavigationView.isVisible = true
                }
            }
        }
        handleNotification()
    }

    private fun setNightMode() {
        if (checkIfNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun handleNotification() {
        if(intent.extras?.containsKey(NOTIFICATION_ID) == true) {
            val notificationId = intent.extras?.getString(NOTIFICATION_ID)
            navController.navigate(R.id.notificationFragment, bundleOf(NOTIFICATION_ID to notificationId))
        }
    }

    private fun hideSystemUI() {
        window.statusBarColor =  ContextCompat.getColor(this, R.color.transparent)
        WindowCompat.getInsetsController(window, binding.root).isAppearanceLightStatusBars = false
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, binding.root).hide(WindowInsetsCompat.Type.ime())
    }

    private fun showSystemUI() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        WindowCompat.getInsetsController(window, binding.root).isAppearanceLightStatusBars = true
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun showSystemUIOnNightMode() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        WindowCompat.getInsetsController(window, binding.root).isAppearanceLightStatusBars = false
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    fun navigateTo(searchFragment: Int) {
        binding.bottomNavigationView.selectedItemId = searchFragment
    }

    fun checkIfNightMode(): Boolean {
        val sharedPref = getSharedPreferences("sharedFile", Context.MODE_PRIVATE)
        return if(sharedPref.contains("isDarkModeActive")) {
            sharedPref.getBoolean("isDarkModeActive", false)
        }
        else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleManager(this).setLocale(this)
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null)
            super.attachBaseContext(LocaleManager(newBase).setLocale(newBase))
        else
            super.attachBaseContext(newBase)
    }
}