package com.poly_team.fnews.view.auth.sign_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poly_team.fnews.R
import com.poly_team.fnews.data.repository.AuthRepository
import com.poly_team.fnews.data.repository.AuthRepository.Companion.FACEBOOK_ACCOUNT
import com.poly_team.fnews.data.repository.AuthRepository.Companion.GOOGLE_ACCOUNT
import com.poly_team.fnews.utility.isNotValidPassword
import com.poly_team.fnews.utility.isNotValidUsername
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mApp: Application,
    private val mAuthRepository: AuthRepository
) : BaseViewModel() {

    companion object {
        const val LOGIN_SUCCESS = 1
        const val NAV_REGISTER_SCREEN = 2
        const val NAV_FORGOT_SCREEN = 3
    }

    val mUsername = MutableLiveData("")
    val mPassword = MutableLiveData("")
    val mErrorMsg = MutableLiveData("")


     override var mHandleException =
        CoroutineExceptionHandler { _, t ->
            run {
                t.printStackTrace()
                mErrorMsg.value = t.message
            }
        }


    fun login() {

        mErrorMsg.value = ""
        mEvent.value = HIDE_KEYBOARD_EVENT

        val username = mUsername.value!!
        val password = mPassword.value!!

        if (username.isEmpty()) {
            mErrorMsg.value = mApp.getString(R.string.empty_username)
            return
        }

        if (username.isNotValidUsername()) {
            mErrorMsg.value = mApp.getString(R.string.invalid_username)
            return
        }

        if (password.isEmpty()) {
            mErrorMsg.value = mApp.getString(R.string.empty_password)
            return
        }

        if (password.isNotValidPassword()) {
            mErrorMsg.value = mApp.getString(R.string.invalid_password)
            return
        }

        mLoading.value = true
        val job = viewModelScope.launch(mHandleException) {
            val success = mAuthRepository.login(username, password)
            if (success) {
                mErrorMsg.value = ""
                mEvent.value = LOGIN_SUCCESS
            } else {
                mErrorMsg.value = mApp.getString(R.string.login_fail)
            }
        }

        job.invokeOnCompletion { mLoading.value = false }
    }

    fun loginWithFacebook(accountId: String?, displayName: String?, avatar: String?) {

        if (accountId.isNullOrBlank() || displayName.isNullOrBlank() || avatar.isNullOrBlank()) {
            mErrorMsg.value = mApp.getString(R.string.facebook_fail)
            return
        }

        mLoading.value = true
        val job = viewModelScope.launch(mHandleException) {
            val success = mAuthRepository.loginWith(FACEBOOK_ACCOUNT, accountId, displayName, avatar, null)
            if(success) {
                mEvent.value = LOGIN_SUCCESS
            } else {
                mErrorMsg.value = mApp.getString(R.string.facebook_fail)
            }
        }
        job.invokeOnCompletion { mLoading.value = false }
    }

    fun loginWithGoogle(accountId: String?, displayName: String?, avatar: String?, email: String?) {

        if (accountId.isNullOrBlank() || displayName.isNullOrBlank() || avatar.isNullOrBlank() || email.isNullOrBlank()) {
            mErrorMsg.value = mApp.getString(R.string.google_fail)
            return
        }

        mLoading.value = true
        val job = viewModelScope.launch(mHandleException) {
            val success = mAuthRepository.loginWith(GOOGLE_ACCOUNT, accountId, displayName, avatar, email)
            if(success) {
                mEvent.value = LOGIN_SUCCESS
            } else {
                mErrorMsg.value = mApp.getString(R.string.facebook_fail)
            }
        }
        job.invokeOnCompletion { mLoading.value = false }
    }

    fun navRegisterScreen() {
        mEvent.value = NAV_REGISTER_SCREEN
    }

    fun navForgetScreen() {
        mEvent.value = NAV_FORGOT_SCREEN
    }

    class LoginViewModelFactory(
        private var mApp: Application, private val mAuthRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(mApp, mAuthRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }

}