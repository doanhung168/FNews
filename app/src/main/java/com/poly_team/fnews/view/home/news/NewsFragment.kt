package com.poly_team.fnews.view.home.news

import android.os.Bundle
import android.view.View
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsBinding
import com.poly_team.fnews.view.BaseFragment

class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    override fun getLayout() = R.layout.fragment_news
    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {
        mBinding?.contentToolbar?.setPadding(0, top, 0, 0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}