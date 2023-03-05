package com.poly_team.fnews.view.welcome

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentWelcomeBinding
import com.poly_team.fnews.view.BaseFragment


class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    override fun getLayout() = R.layout.fragment_welcome

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupIndicatorWithViewPager()
        setupSkipPage()
        setupNextPage()
    }

    private fun setupViewPager() {
        val colorIndicator = ResourcesCompat.getDrawable(
            resources, R.drawable.bg_color_circle, null
        )
        val nonColorIndicator = ResourcesCompat.getDrawable(
            resources, R.drawable.bg_circle, null
        )

        with(mBinding!!) {
            viewpager.adapter = WelcomeViewPagerAdapter(requireActivity())
            viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            v1.background = colorIndicator
                            v2.background = nonColorIndicator
                            v3.background = nonColorIndicator
                            tvSkip.visibility = View.VISIBLE
                            tvNext.visibility = View.VISIBLE
                        }
                        1 -> {
                            v1.background = nonColorIndicator
                            v2.background = colorIndicator
                            v3.background = nonColorIndicator
                            tvSkip.visibility = View.VISIBLE
                            tvNext.visibility = View.VISIBLE
                        }
                        2 -> {
                            v1.background = nonColorIndicator
                            v2.background = nonColorIndicator
                            v3.background = colorIndicator
                            tvSkip.visibility = View.GONE
                            tvNext.visibility = View.GONE
                        }
                    }
                }
            })
        }
    }

    private fun setupIndicatorWithViewPager() {
        with(mBinding!!) {
            v1.setOnClickListener { viewpager.currentItem = 0 }
            v2.setOnClickListener { viewpager.currentItem = 1 }
            v3.setOnClickListener { viewpager.currentItem = 2 }
        }

    }

    private fun setupSkipPage() {
        with(mBinding!!) {
            tvSkip.setOnClickListener { viewpager.currentItem = 2 }
        }

    }

    private fun setupNextPage() {
        with(mBinding!!) {
            tvNext.setOnClickListener {
                if (viewpager.currentItem < 2) {
                    viewpager.currentItem = viewpager.currentItem + 1
                }
            }
        }
    }


}