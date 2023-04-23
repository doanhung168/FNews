package com.poly_team.fnews.view.home.news.news_detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.navigation.fragment.navArgs
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsContentBinding
import com.poly_team.fnews.view.BaseFragment

class NewsContentFragment : BaseFragment<FragmentNewsContentBinding>() {

    private val TAG = "NewsContentFragment"

    private val mArgs: NewsContentFragmentArgs by navArgs()

    override fun getLayout(): Int = R.layout.fragment_news_content

    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mArgs.media?.let { media ->
            media.content?.let { content ->
                Log.i(TAG, "onViewCreated: $content")
                mBinding?.webView?.apply {
                    settings.javaScriptEnabled = true
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.defaultFontSize = 18
                    loadData(content, "text/html", "UTF-8")
                }
            }
        }

    }

}