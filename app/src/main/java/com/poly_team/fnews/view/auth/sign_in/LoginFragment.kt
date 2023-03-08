package com.poly_team.fnews.view.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import com.facebook.FacebookException
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentLoginBinding
import com.poly_team.fnews.utility.*
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.BaseViewModel.Companion.HIDE_KEYBOARD_EVENT
import com.poly_team.fnews.view.auth.sign_in.LoginViewModel.Companion.LOGIN_SUCCESS
import com.poly_team.fnews.view.auth.sign_in.LoginViewModel.Companion.NAV_FORGOT_SCREEN
import com.poly_team.fnews.view.auth.sign_in.LoginViewModel.Companion.NAV_REGISTER_SCREEN
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    companion object {
        private const val TAG = "LoginFragment"
    }

    private val mLoginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var mFacebookComponent: FacebookComponent

    @Inject
    lateinit var mGoogleComponent: GoogleComponent


    override fun getLayout() = R.layout.fragment_login

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        listenEvent()
        loginWithFbAction()
        loginWithGgAction()
    }

    private fun setupViewModel() {
        with(mBinding!!) {
            lifecycleOwner = this@LoginFragment.viewLifecycleOwner
            viewModel = this@LoginFragment.mLoginViewModel
        }

    }

    private fun loginWithFbAction() {
        mBinding!!.imvFacebook.setOnClickListener {
            mFacebookComponent.login(this.activity, object : FacebookLoginCompCallback {
                override fun onCancel() {
                    Log.i(TAG, "onCancel: ")
                }

                override fun onError(error: FacebookException) {
                    with(mBinding!!) {
                        tvError.visibility = View.VISIBLE
                        tvError.text = error.message
                    }
                }

                override fun onSuccess(id: String?, name: String?, avatar: String?) {
                    mLoginViewModel.loginWithFacebook(id, name, avatar)
                }

            })
        }
    }

    private fun loginWithGgAction() {
        mBinding!!.imvGoogle.setOnClickListener {
            mGoogleComponent.login(this.requireActivity(), object : GoogleComponentCallback {
                override fun onError(error: Exception) {
                    with(mBinding!!) {
                        tvError.visibility = View.VISIBLE
                        tvError.text = error.message
                    }
                }

                override fun onSuccess(
                    id: String?, displayName: String?, avatar: String?, email: String?
                ) {
                    mLoginViewModel.loginWithGoogle(id, displayName, avatar, email)
                }

            })
        }
    }


    private fun listenEvent() {
        with(mLoginViewModel) {
            _mEvent.observe(viewLifecycleOwner) { message ->
                when (message) {
                    LOGIN_SUCCESS -> {
                        mBinding!!.tvError.visibility = View.VISIBLE
                        mBinding!!.tvError.text = "Success"
                    }
                    NAV_REGISTER_SCREEN -> {
                        mNavController.navigate(R.id.registerFragment)
                    }
                    NAV_FORGOT_SCREEN -> {

                    }
                    HIDE_KEYBOARD_EVENT -> {
                        hideKeyboard(requireActivity())
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


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mGoogleComponent.mCallbackManager?.onActivityResult(requestCode, resultCode, data)
        mFacebookComponent.mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
    }


}