package com.poly_team.fnews.view.welcome

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class WelcomeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Welcome1Fragment()
            1 -> Welcome2Fragment()
            2 -> Welcome3Fragment()
            else -> Welcome1Fragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}