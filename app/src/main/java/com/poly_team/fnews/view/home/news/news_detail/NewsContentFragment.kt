package com.poly_team.fnews.view.home.news.news_detail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentNewsContentBinding
import com.poly_team.fnews.utility.text
import com.poly_team.fnews.view.BaseViewModel
import com.poly_team.fnews.view.BaseViewModel.Companion.COMMENT_FAILURE
import com.poly_team.fnews.view.BaseViewModel.Companion.COMMENT_SUCCESSFULLY
import com.poly_team.fnews.view.BaseViewModel.Companion.NO_AUTH_EVENT
import com.poly_team.fnews.view.home.*
import com.poly_team.fnews.view.home.ReadNewsService.Companion.LOOP_ACTION
import com.poly_team.fnews.view.home.ReadNewsService.Companion.LOOP_KEY
import com.poly_team.fnews.view.home.ReadNewsService.Companion.NEXT_ACTION
import com.poly_team.fnews.view.home.ReadNewsService.Companion.PLAY_ACTION
import com.poly_team.fnews.view.home.ReadNewsService.Companion.PLAY_KEY
import com.poly_team.fnews.view.home.ReadNewsService.Companion.PREVIOUS_ACTION
import com.poly_team.fnews.view.home.ReadNewsService.Companion.REQUEST_UPDATE_STATE
import com.poly_team.fnews.view.home.ReadNewsService.Companion.SER_RUNNING_KEY
import com.poly_team.fnews.view.home.ReadNewsService.Companion.STOP_ACTION
import com.poly_team.fnews.view.home.ReadNewsService.Companion.TITLE_KEY
import com.poly_team.fnews.view.home.ReadNewsService.Companion.UPDATE_STATE
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsContentFragment : HomeBaseFragment<FragmentNewsContentBinding>() {

    private val TAG = "NewsContentFragment"

    private val mArgs: NewsContentFragmentArgs by navArgs()

    private val mNewsContentViewModel : NewsContentViewModel by activityViewModels()

    private lateinit var mRelativeNewsAdapter: RelativeNewsAdapter

    private lateinit var mNewsCommentFragment: NewsCommentFragment

    private val mSpeakBroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.action == UPDATE_STATE) {
                val title = intent.extras?.getString(TITLE_KEY)
                val isPlaying = intent.extras?.getBoolean(PLAY_KEY)
                val isLooping = intent.extras?.getBoolean(LOOP_KEY)
                val isRunning = intent.extras?.getBoolean(SER_RUNNING_KEY)?: false

                if(isRunning) {
                    updateSpeakNewsBar(title?: "" , isPlaying?: false, isLooping?: false, true)
                } else {
                    updateSpeakNewsBar("" , isPlaying?: false, isLooping?: false, false)
                }

            }
        }
    }

    override fun getLayout(): Int = R.layout.fragment_news_content

    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {
        mBinding?.toolbar?.setPadding(0, top, 0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(mSpeakBroadcastReceiver, IntentFilter(UPDATE_STATE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        bottomNavVisible(false)
        speakNewsBarInHome(false)
        setUpSpeakNewsBar()
        setUpRelativeNewsRcv()
        receiverDataFromUserAction()
        setUpCommentBottomSheet()
        listenerViewModel()
        setUpActionBtn()
    }

    private fun setUpView() {
        mBinding?.webView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                mBinding?.relativeNews?.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpActionBtn() {

        mBinding?.apply {
            loHearNews.setOnClickListener {
                mNewsContentViewModel.speakText()
            }

            loCommentNews.setOnClickListener {
                mNewsCommentFragment.show(childFragmentManager, "CommentFragment")
            }
        }

    }
    private fun setUpCommentBottomSheet() {
        mNewsCommentFragment = NewsCommentFragment(mNewsContentViewModel)
    }
    private fun setUpRelativeNewsRcv() {
        mRelativeNewsAdapter = RelativeNewsAdapter()
        mRelativeNewsAdapter.setListener {
            Log.i(TAG, "setUpRelativeNewsRcv: ${it.title}")
        }
        mBinding?.rcvRelativeNewsList?.adapter = mRelativeNewsAdapter
    }

    private fun speakNewsBarInHome(visible: Boolean) {
        val homeFragment = requireParentFragment().parentFragment as HomeFragment
        homeFragment.speakNewsBarVisible(visible)
    }


    private fun bottomNavVisible(visible: Boolean) {
        val homeFragment = requireParentFragment().parentFragment as HomeFragment
        homeFragment.bottomNavVisible(visible)
    }

    private fun receiverDataFromUserAction() {
        mArgs.news?.let { news ->
            mNewsContentViewModel.setCurrentNews(news)
        }
    }

    private fun listenerViewModel() {
        mNewsContentViewModel._mCurrentNews.observe(viewLifecycleOwner) {
            Log.i(TAG, "current news: $it")
            mBinding?.news = it
        }

        mNewsContentViewModel._mRelativeNews.observe(viewLifecycleOwner) {
            Log.i(TAG, " relative news list: ${it.size}")
            mRelativeNewsAdapter.submitList(it)
        }

        mNewsContentViewModel._mEvent.observe(viewLifecycleOwner) {
            when(it) {
                NO_AUTH_EVENT -> {
                    Log.i(TAG, "no auth for comment")
                }

                COMMENT_SUCCESSFULLY -> {
                    mNewsCommentFragment.dismiss()
                    Toast.makeText(context, "Bình luận thành công!\nVui lòng chờ phê duyệt", Toast.LENGTH_LONG).show()
                }

                COMMENT_FAILURE -> {
                    Log.e(TAG, "Comment failure")
                }

            }
        }
    }

    private fun updateSpeakNewsBar(title: String, isPlaying: Boolean, isLoop: Boolean, visible: Boolean) {
        mBinding?.tvTitle?.text = title

        if(isPlaying) {
            mBinding?.imvPlay?.setImageResource(R.drawable.ic_pause_circle_outline)
        } else {
            mBinding?.imvPlay?.setImageResource(R.drawable.ic_play_circle_outline)
        }

        if(isLoop) {
            mBinding?.imvLoop?.setImageResource(R.drawable.ic_repeat_one)
        } else {
            mBinding?.imvLoop?.setImageResource(R.drawable.ic_repeat_more)
        }

        if(visible) {
            mBinding?.speakNewsBarInContent?.visibility = View.VISIBLE
        } else {
            mBinding?.speakNewsBarInContent?.visibility = View.GONE
        }
    }

    private fun setUpSpeakNewsBar() {

        mBinding?.tvTitle?.isSelected = true
        mBinding?.imvLoop?.setOnClickListener {
            context?.sendBroadcast(Intent(LOOP_ACTION))
        }

        mBinding?.imvPlay?.setOnClickListener {
            context?.sendBroadcast(Intent(PLAY_ACTION))
        }

        mBinding?.imvNext?.setOnClickListener {
            context?.sendBroadcast(Intent(NEXT_ACTION))
        }

        mBinding?.imvPrevious?.setOnClickListener {
            context?.sendBroadcast(Intent(PREVIOUS_ACTION))
        }

        mBinding?.imvStop?.setOnClickListener {
            context?.sendBroadcast(Intent(STOP_ACTION))
        }

        if(isMyServiceRunning(ReadNewsService::class.java)) {
            val intent = Intent(context, ReadNewsService::class.java)
            intent.action = REQUEST_UPDATE_STATE
            context?.startService(intent)
        }
    }

    override fun onDestroyView() {
        context?.unregisterReceiver(mSpeakBroadcastReceiver)
        super.onDestroyView()
        bottomNavVisible(true)

        val homeFragment = requireParentFragment().parentFragment as HomeFragment
        if(homeFragment.mSpeakingBarShouldBeVisible) {
            speakNewsBarInHome(true)
        }

    }



}