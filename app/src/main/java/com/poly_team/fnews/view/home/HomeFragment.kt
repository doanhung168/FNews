package com.poly_team.fnews.view.home

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentHomeBinding
import com.poly_team.fnews.view.BaseFragment
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

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val TAG = "HomeFragment"

    private var mAllowSpeakNewsVisibleFromChildren = true

    var mSpeakingBarShouldBeVisible = false
    private set

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


    override fun getLayout() = R.layout.fragment_home
    override fun setInsets(left: Int, top: Int, right: Int, bottom: Int) {
        mBinding!!.root.updateLayoutParams<MarginLayoutParams> {
            bottomMargin = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.registerReceiver(mSpeakBroadcastReceiver, IntentFilter(UPDATE_STATE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpeakNewsBar()
        setupBottomBar()
    }

    private fun setupBottomBar() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.home_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        mBinding!!.bottomBar.setupWithNavController(navController)
    }

    fun bottomNavVisible(visible: Boolean) {
        if(visible) {
            mBinding?.bottomBar?.visibility = View.VISIBLE
        } else {
            mBinding?.bottomBar?.visibility = View.GONE
        }
    }

    fun speakNewsBarVisible(visible: Boolean) {
        if(visible) {
            mAllowSpeakNewsVisibleFromChildren = true
            mBinding?.speakNewsBar?.visibility = View.VISIBLE
        } else {
            mAllowSpeakNewsVisibleFromChildren = false
            mBinding?.speakNewsBar?.visibility = View.GONE
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

        mSpeakingBarShouldBeVisible = visible
        if(visible && mAllowSpeakNewsVisibleFromChildren) {
            mBinding?.speakNewsBar?.visibility = View.VISIBLE
        } else {
            mBinding?.speakNewsBar?.visibility = View.GONE
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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onDestroy() {
        super.onDestroy()
        context?.unregisterReceiver(mSpeakBroadcastReceiver)
    }



}