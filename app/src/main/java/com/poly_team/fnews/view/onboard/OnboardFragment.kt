package com.poly_team.fnews.view.onboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentOnboardBinding
import com.poly_team.fnews.view.BaseFragment


class OnboardFragment : BaseFragment<FragmentOnboardBinding>() {
    override fun getLayout() = R.layout.fragment_onboard

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper())
            .postDelayed({ mNavController.navigate(R.id.welcomeFragment) }, 1500)
    }
}