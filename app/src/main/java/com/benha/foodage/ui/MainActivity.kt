package com.benha.foodage.ui

import android.os.Build
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.benha.foodage.R
import com.benha.foodage.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        handleSplashScreen()
        super.onCreate(savedInstanceState)

        initializeUI()
        setupNavigation()
    }

    private fun handleSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Animate splash screen slide-up exit
                splashScreenView
                    .animate()
                    .translationY(-splashScreenView.height.toFloat())
                    .setInterpolator(DecelerateInterpolator(1.5f))
                    .setDuration(300)
                    .withEndAction {
                        splashScreenView.remove()
                    }
                    .start()
            }
        }
    }


    private fun initializeUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            show()
            title = "Foodage"
        }
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.frag_home) as NavHostFragment
        navController = navHostFragment.navController

        binding.navMenu.setupWithNavController(navController)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                navController.currentBackStackEntryFlow.collect { entry ->
                    val destination = entry.destination
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}