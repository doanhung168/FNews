package com.poly_team.fnews.view.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentHomeBinding
import com.poly_team.fnews.utility.updateMarginWithSystemInsets
import com.poly_team.fnews.view.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayout() = R.layout.fragment_home


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar()
        updateMarginWithSystemInsets(mBinding!!.root, false)
    }

    private fun setupBottomBar() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        mBinding!!.bottomBar.setupWithNavController(navController)
    }
}