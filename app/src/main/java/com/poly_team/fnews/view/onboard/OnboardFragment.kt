package com.poly_team.fnews.view.onboard

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentOnboardBinding
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.onboard.OnboardViewModel.Companion.AUTO_LOGIN_FAILURE
import com.poly_team.fnews.view.onboard.OnboardViewModel.Companion.AUTO_LOGIN_SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardFragment : BaseFragment<FragmentOnboardBinding>() {

    private val mViewModel: OnboardViewModel by viewModels()

    override fun getLayout() = R.layout.fragment_onboard

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenEvent()

        Handler(Looper.getMainLooper()).postDelayed({
            direction()
        }, 500)

    }

    private fun listenEvent() {
        mViewModel._mEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                AUTO_LOGIN_FAILURE -> {
                    mNavController.navigate(R.id.action_onboardFragment_to_loginFragment)
                }
                AUTO_LOGIN_SUCCESS -> {
                    mNavController.navigate(R.id.action_onboardFragment_to_homeFragment)
                }
            }
        }
    }

    private fun direction() {
        if (mViewModel.getIsRunFirstTime()) {
            mNavController.navigate(R.id.action_onboardFragment_to_welcomeFragment)
            return
        }

        val token = mViewModel.getAuthToken()
        if (token == null) {
            mNavController.navigate(R.id.action_onboardFragment_to_loginFragment)
        } else {
            mViewModel.autoLogin(token)
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}