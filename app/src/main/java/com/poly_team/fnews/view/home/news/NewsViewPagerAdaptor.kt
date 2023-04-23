package com.poly_team.fnews.view.home.news


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.poly_team.fnews.data.model.Field


class NewsViewPagerAdaptor(mFragment: Fragment, private val mFields: List<Field>) : FragmentStateAdapter(mFragment) {

    override fun getItemCount(): Int = mFields.size

    override fun createFragment(position: Int): Fragment {
        val fragment = NewsViewPagerFragment()
        val bundle = Bundle()
        bundle.putString("fieldId", mFields[position].id)
        fragment.arguments = bundle
        return fragment
    }
}