package com.poly_team.fnews.view.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentHomeBinding
import com.poly_team.fnews.view.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getLayout() = R.layout.fragment_home
    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {
        mBinding!!.root.updateLayoutParams<MarginLayoutParams> {
            bottomMargin = 0
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar()
    }

    private fun setupBottomBar() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        mBinding!!.bottomBar.setupWithNavController(navController)
    }
}