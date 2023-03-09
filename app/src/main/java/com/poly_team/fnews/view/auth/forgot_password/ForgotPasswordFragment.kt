package com.poly_team.fnews.view.auth.forgot_password

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentForgotPasswordBinding
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.auth.forgot_password.ForgotPasswordViewModel.Companion.TASK_SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private val mViewModel: ForgotPasswordViewModel by viewModels()
    override fun getLayout(): Int = R.layout.fragment_forgot_password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupViewModel()
        listenEvent()
    }

    private fun setupToolbar() {
        mBinding!!.toolbar.setNavigationOnClickListener {
            mNavController.popBackStack()
        }
    }

    private fun setupViewModel() {
        with(mBinding!!) {
            lifecycleOwner = this@ForgotPasswordFragment.viewLifecycleOwner
            viewModel = this@ForgotPasswordFragment.mViewModel
        }
    }

    private fun listenEvent() {
        with(mViewModel) {
            _mEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    TASK_SUCCESS -> {
                        showDialog()
                    }
                }
            }
            _mLoading.observe(viewLifecycleOwner) { loading ->
                if (loading) {
                    disableScreen()
                    mBinding!!.progressBar.visibility = View.VISIBLE
                } else {
                    enableScreen()
                    mBinding!!.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.watch_email_to_find_password)).setPositiveButton(
                R.string.ok
            ) { dialog, _ ->
                mViewModel.clear()
                dialog?.dismiss()
            }.setCancelable(false).create().show()
    }


}