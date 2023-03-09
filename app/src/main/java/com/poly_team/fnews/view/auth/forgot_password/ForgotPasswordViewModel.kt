package com.poly_team.fnews.view.auth.forgot_password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.poly_team.fnews.R
import com.poly_team.fnews.data.repository.AuthRepository
import com.poly_team.fnews.utility.isNotValidEmail
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val mApp: Application,
    private val mAuthRepository: AuthRepository
) : BaseViewModel() {

    companion object {
        const val TASK_SUCCESS = 1
    }

    val mEmail = MutableLiveData("")
    val mErrorMsg = MutableLiveData("")


    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            t.printStackTrace()
            mErrorMsg.value = t.message
        }
    }

    fun findPassword() {
        mErrorMsg.value = ""
        mEvent.value = HIDE_KEYBOARD_EVENT

        val email = mEmail.value!!

        if (email.isEmpty()) {
            mErrorMsg.value = mApp.getString(R.string.empty_email)
            return
        }

        if (email.isNotValidEmail()) {
            mErrorMsg.value = mApp.getString(R.string.invalid_email)
            return
        }

        mLoading.value = true
        val job = viewModelScope.launch(mHandleException) {
            val response = mAuthRepository.findPassword(email)
            if (response.success) {
                mEvent.value = TASK_SUCCESS
            } else {
                response.message?.let { mErrorMsg.value = it }
            }
        }
        job.invokeOnCompletion { mLoading.value = false }

    }


    fun clear() {
        mEmail.value = ""
    }


}