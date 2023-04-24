package com.poly_team.fnews.view.home.news


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsViewPagerBinding
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.home.HomeBaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NewsViewPagerFragment : HomeBaseFragment<FragmentNewsViewPagerBinding>() {

    private val TAG = "NewsViewPagerFragment"

    private var mLoadFlag = false

    private val mNewsViewModel: NewsViewModel by activityViewModels()

    @Inject
    lateinit var mNewsAdapter: NewsAdapter

    override fun getLayout(): Int = R.layout.fragment_news_view_pager

    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        mBinding?.lifecycleOwner = viewLifecycleOwner
        setupRecycleView()
        loadData()
        setupRefreshLayout()
        return mBinding?.root
    }

    private fun loadData() {
        arguments?.getString("fieldId")?.let {
            lifecycleScope.launch {
                mNewsViewModel.mNewsList[it]?.collectLatest {
                    mLoadFlag = true
                    mNewsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                }
            }
        }
    }

    private fun setupRefreshLayout() {
        mBinding?.apply {
            refreshLayout.setOnRefreshListener {
                arguments?.getString("fieldId")?.let {
                    mNewsViewModel.loadData(it)
                    lifecycleScope.launch {
                        mNewsViewModel.mNewsList[it]?.collectLatest {
                            mLoadFlag = true
                            refreshLayout.isRefreshing = false
                            mNewsAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycleView() {
        with(mBinding!!) {
            if(mLoadFlag) {
                stopShimmerLayout()
            }

            mNewsAdapter.setListener {
                val action = NewsFragmentDirections.actionNewsFragmentToNewsContentFragment(it)
                mNavController.navigate(action)
            }
            rcvNews.adapter = mNewsAdapter

            mNewsAdapter.addOnPagesUpdatedListener {
                stopShimmerLayout()
            }
        }
    }

    private fun stopShimmerLayout() {
        mBinding?.apply {
            shimmerLayout.visibility = View.INVISIBLE
            shimmerLayout.stopShimmer()
            rcvNews.visibility = View.VISIBLE
        }
    }

    override fun onDetach() {
        super.onDetach()
        mLoadFlag = false
    }




}