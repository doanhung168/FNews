package com.poly_team.fnews.view.auth.register

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poly_team.fnews.R
import com.poly_team.fnews.data.repository.AuthRepository
import com.poly_team.fnews.utility.*
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val mApp: Application,
    private val mAuthRepository: AuthRepository
) : BaseViewModel() {

    companion object {
        const val REGISTER_SUCCESS = 1
        const val LOGIN_SUCCESS = 2
    }

    val mUsername = MutableLiveData("")
    val mEmail = MutableLiveData("")
    val mPassword = MutableLiveData("")
    val mConfirmPassword = MutableLiveData("")
    val mCheckPolicy = MutableLiveData(false)
    val mErrorMsg = MutableLiveData("")

    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            t.printStackTrace()
            mErrorMsg.value = t.message
        }
    }


    fun register() {

        mErrorMsg.value = ""
        mEvent.value = HIDE_KEYBOARD_EVENT

        val username = mUsername.value!!
        val password = mPassword.value!!
        val confirmPassword = mConfirmPassword.value!!
        val email = mEmail.value!!
        val checkPolicy = mCheckPolicy.value!!

        if (username.isEmpty()) {
            mErrorMsg.value = mApp.getString(R.string.empty_username)
            return
        }

        if (username.isNotValidUsername()) {
            mErrorMsg.value = mApp.getString(R.string.invalid_username)
            return
        }

        if (email.isEmpty()) {
            mErrorMsg.value = mApp.getString(R.string.empty_email)
            return
        }

        if (email.isNotValidEmail()) {
            mErrorMsg.value = mApp.getString(R.string.invalid_email)
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

        if (confirmPassword != password) {
            mErrorMsg.value = mApp.getString(R.string.invalid_confirm_password)
            return
        }

        if (!checkPolicy) {
            mErrorMsg.value = mApp.getString(R.string.invalid_check_policy)
            return
        }

        mLoading.value = true
        val job = viewModelScope.launch(mHandleException) {
            val response = mAuthRepository.register(username, password, email)
            if (response.success) {
                mErrorMsg.value = ""
                mEvent.value = REGISTER_SUCCESS
            } else {
                val message = response.message
                message?.let { mErrorMsg.value = message }
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
            val success = mAuthRepository.loginWith(
                AuthRepository.FACEBOOK_ACCOUNT, accountId, displayName, avatar, null
            )
            if (success) {
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
            val success = mAuthRepository.loginWith(
                AuthRepository.GOOGLE_ACCOUNT, accountId, displayName, avatar, email
            )
            if (success) {
                mEvent.value = LOGIN_SUCCESS
            } else {
                mErrorMsg.value = mApp.getString(R.string.facebook_fail)
            }
        }
        job.invokeOnCompletion { mLoading.value = false }
    }

    @Suppress("UNCHECKED_CAST")
    class RegisterViewModelFactory(
        private var mApp: Application,
        private val mAuthRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(mApp, mAuthRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }


}