package com.poly_team.fnews.view.home.news

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsBinding
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.auth.register.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private lateinit var mNewsViewPagerAdaptor: NewsViewPagerAdaptor

    private val mViewModel: NewsViewModel by activityViewModels()

    override fun getLayout() = R.layout.fragment_news
    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {
        mBinding?.toolbar?.setPadding(0, top + 24, 0, 24)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewpager()
    }

    private fun setupViewpager() {
        mBinding?.apply {
            mViewModel._mFieldList.observe(viewLifecycleOwner) {fieldList ->
                mNewsViewPagerAdaptor = NewsViewPagerAdaptor(this@NewsFragment, fieldList)
                viewPagerNews.offscreenPageLimit = 8
                viewPagerNews.adapter = mNewsViewPagerAdaptor
                TabLayoutMediator(tabLayoutField, viewPagerNews) { tab, position ->
                    tab.text = fieldList[position].value
                }.attach()
            }
        }
    }


}