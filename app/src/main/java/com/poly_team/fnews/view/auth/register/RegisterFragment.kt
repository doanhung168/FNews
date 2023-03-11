package com.poly_team.fnews.view.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.facebook.FacebookException
import com.poly_team.fnews.R
import com.poly_team.fnews.databinding.FragmentRegisterBinding
import com.poly_team.fnews.utility.*
import com.poly_team.fnews.view.BaseFragment
import com.poly_team.fnews.view.BaseViewModel.Companion.HIDE_KEYBOARD_EVENT
import com.poly_team.fnews.view.auth.register.RegisterViewModel.Companion.LOGIN_SUCCESS
import com.poly_team.fnews.view.auth.register.RegisterViewModel.Companion.REGISTER_SUCCESS
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    companion object {
        private const val TAG = "RegisterFragment"
    }

    private val mRegisterViewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var mFacebookComponent: FacebookComponent

    @Inject
    lateinit var mGoogleComponent: GoogleComponent

    override fun getLayout() = R.layout.fragment_register

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePaddingWithSystemInsets(mBinding!!.root)
        setupViewModel()
        listenEvent()
        loginWithFbAction()
        loginWithGgAction()
    }

    private fun setupViewModel() {
        with(mBinding!!) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mRegisterViewModel
        }
    }

    private fun listenEvent() {
        with(mRegisterViewModel) {
            _mEvent.observe(viewLifecycleOwner) { message ->
                when (message) {
                    REGISTER_SUCCESS -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.register_success),
                            Toast.LENGTH_LONG
                        ).show()
                        mNavController.popBackStack()
                    }
                    LOGIN_SUCCESS -> {
                        mBinding!!.tvError.visibility = View.VISIBLE
                        mBinding!!.tvError.text = "Success"
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

    private fun loginWithFbAction() {
        mBinding!!.imvFacebook.setOnClickListener {
            mFacebookComponent.login(requireActivity(), object : FacebookLoginCompCallback {
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
                    mRegisterViewModel.loginWithFacebook(id, name, avatar)
                }

            })
        }
    }

    private fun loginWithGgAction() {
        mBinding!!.imvGoogle.setOnClickListener {
            mGoogleComponent.login(requireActivity(), object : GoogleComponentCallback {
                override fun onError(error: Exception) {
                    with(mBinding!!) {
                        tvError.visibility = View.VISIBLE
                        tvError.text = error.message
                    }
                }

                override fun onSuccess(
                    id: String?, displayName: String?, avatar: String?, email: String?
                ) {
                    mRegisterViewModel.loginWithGoogle(id, displayName, avatar, email)
                }

            })
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mFacebookComponent.mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data)
        mGoogleComponent.mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}