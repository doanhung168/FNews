package com.poly_team.fnews.view.home.news


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsViewPagerBinding
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.home.HomeFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsViewPagerFragment : BaseFragment<FragmentNewsViewPagerBinding>() {

    private val TAG = "NewsViewPagerFragment"

    private val mNewsViewModel: NewsViewModel by activityViewModels()

    @Inject
    lateinit var mNewsAdapter: NewsAdapter

    override fun getLayout(): Int = R.layout.fragment_news_view_pager

    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.lifecycleOwner = viewLifecycleOwner
        setupRecycleView()

        arguments?.getString("fieldId")?.let {
            lifecycleScope.launch {
                Log.e(TAG, "onViewCreated: $it")
                mNewsViewModel.mNewsList[it]?.collectLatest {
                    mNewsAdapter.submitData(it)
                }
            }
        }
    }

    private fun setupRecycleView() {
        with(mBinding!!) {
            mNewsAdapter.setListener {
                val action = HomeFragmentDirections.actionHomeFragmentToNewsContentFragment(it)
                mNavController.navigate(action)
            }
            rcvNews.adapter = mNewsAdapter
        }
    }


}