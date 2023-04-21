package com.poly_team.fnews.view.home.news


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class NewsViewPagerAdaptor(mFragment: Fragment) : FragmentStateAdapter(mFragment) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsViewPagerFragment()
        val bundle = Bundle()
        when(position) {
            0 -> {
                bundle.putString("field", "Hot")
                fragment.arguments = bundle
            }
            1 -> {
                bundle.putString("field", "Competition")
                fragment.arguments = bundle
            }
            2 -> {
                bundle.putString("field", "Admissions")
                fragment.arguments = bundle
            }
            3 -> {
                bundle.putString("field", "Sport")
                fragment.arguments = bundle
            }
            4 -> {
                bundle.putString("field", "Exchange")
                fragment.arguments = bundle
            }
        }
        return fragment
    }
}