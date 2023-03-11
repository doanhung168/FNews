package com.poly_team.fnews.view.welcome

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.poly_team.fnews.utility.saveFirstRun
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val mApp: Application
) : BaseViewModel() {

    companion object {
        const val LOGO_TO_LOGIN = 1
    }


    override var mHandleException =
        CoroutineExceptionHandler { _, t -> }

    fun saveRunFirstTime() {
        saveFirstRun(mApp)
        mEvent.value = LOGO_TO_LOGIN
    }

    @Suppress("UNCHECKED_CAST")
    class OnboardViewModelFactory(
        private var mApp: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WelcomeViewModel::class.java)) {
                return WelcomeViewModel(mApp) as T
            }
            throw Exception("Unable construct view_model")
        }
    }

}