package com.poly_team.fnews.view.onboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.poly_team.fnews.data.repository.AuthRepository
import com.poly_team.fnews.utility.getFirstRun
import com.poly_team.fnews.utility.getToken
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val mApp: Application, private val mAuthRepository: AuthRepository
) : BaseViewModel() {

    companion object {
        const val AUTO_LOGIN_SUCCESS = 1
        const val AUTO_LOGIN_FAILURE = 2
    }

    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            t.printStackTrace()
            mEvent.value = AUTO_LOGIN_FAILURE
        }
    }

    fun autoLogin(token: String) {
        viewModelScope.launch(mHandleException) {
            val response = mAuthRepository.autoLogin(token)
            if (response.success) {
                mEvent.value = AUTO_LOGIN_SUCCESS
            } else {
                mEvent.value = AUTO_LOGIN_FAILURE
            }
        }
    }

    fun getIsRunFirstTime(): Boolean {
        return getFirstRun(mApp)
    }

    fun getAuthToken(): String? {
        return getToken(mApp)
    }

    @Suppress("UNCHECKED_CAST")
    class OnboardViewModelFactory(
        private var mApp: Application, private val mAuthRepository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OnboardViewModel::class.java)) {
                return OnboardViewModel(mApp, mAuthRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }


}