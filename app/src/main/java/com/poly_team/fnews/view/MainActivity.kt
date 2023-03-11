package com.poly_team.fnews.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.poly_team.fnews.R
import com.poly_team.fnews.utility.makeFullscreen
import com.poly_team.fnews.view.MainViewModel.Companion.AUTO_LOGIN_FAILURE
import com.poly_team.fnews.view.MainViewModel.Companion.AUTO_LOGIN_SUCCESS
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mViewModel: MainViewModel by viewModels()

    private lateinit var mSplashScreen: SplashScreen

    private lateinit var mNavController: NavController
    private lateinit var mGraph: NavGraph

    private var mKeepSplashScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
        setContentView()
        direction()
    }

    override fun onStart() {
        super.onStart()
        val windowInsetsController =
            WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())
        windowInsetsController.isAppearanceLightNavigationBars = true
    }

    private fun direction() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        mNavController = navHostFragment.navController
        mGraph = mNavController.navInflater.inflate(R.navigation.main_nav_graph)

        if (mViewModel.getIsRunFirstTime()) {
            navigation(R.id.welcomeFragment)
        }

        val token = mViewModel.getAuthToken()
        if (token == null) {
            navigation(R.id.loginFragment)
        } else {
            mViewModel.autoLogin(token)
        }

    }

    private fun setupViewModel() {
        mViewModel._mEvent.observe(this) { event ->
            when (event) {
                AUTO_LOGIN_FAILURE -> {
                    navigation(R.id.loginFragment)
                }
                AUTO_LOGIN_SUCCESS -> {
                    navigation(R.id.homeFragment)
                }
            }
        }
    }

    private fun navigation(destination: Int) {
        mGraph.setStartDestination(destination)
        mNavController.setGraph(mGraph, null)
        mKeepSplashScreen = false
    }

    private fun setContentView() {
        mSplashScreen = installSplashScreen()
        mSplashScreen.setKeepOnScreenCondition { mKeepSplashScreen }
        makeFullscreen(this)
        setContentView(R.layout.activity_main)
        setupSystemInsets()
    }

    private fun setupSystemInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_nav_host)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, 0, 0, insets.bottom)
            WindowInsetsCompat.CONSUMED
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            for (fragment in supportFragmentManager.fragments) {
                val subFragments = fragment.childFragmentManager.fragments
                for (subFragment in subFragments) {
                    subFragment.onActivityResult(requestCode, resultCode, data)
                }
            }
        } catch (e: Exception) {
            Log.d("ERROR", e.toString())
        }
    }
}