package com.poly_team.fnews.view.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentWelcome3Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Welcome3Fragment : Fragment() {

    private var mBinding: FragmentWelcome3Binding? = null
    private val mViewModel: WelcomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentWelcome3Binding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        listenEvent()
    }

    private fun listenEvent() {
        mViewModel._mEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                WelcomeViewModel.LOGO_TO_LOGIN -> {
                    Navigation.findNavController(requireActivity(), R.id.main_nav_host)
                        .navigate(R.id.action_welcomeFragment_to_loginFragment)
                }
            }
        }
    }

    private fun setupViewModel() {
        with(mBinding!!) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}