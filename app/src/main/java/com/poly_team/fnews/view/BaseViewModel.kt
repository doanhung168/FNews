package com.poly_team.fnews.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.poly_team.fnews.utility.SingleLiveEvent
import kotlinx.coroutines.CoroutineExceptionHandler

abstract class BaseViewModel : ViewModel() {

    companion object {
        const val HIDE_KEYBOARD_EVENT = 1
        const val NO_AUTH_EVENT = 2
        const val COMMENT_SUCCESSFULLY = 3
        const val COMMENT_FAILURE = 4
    }

    protected val mEvent = SingleLiveEvent<Int>()
    val _mEvent: LiveData<Int> = mEvent

    protected val mLoading = SingleLiveEvent<Boolean>()
    val _mLoading : LiveData<Boolean> = mLoading

    abstract var mHandleException: CoroutineExceptionHandler

}