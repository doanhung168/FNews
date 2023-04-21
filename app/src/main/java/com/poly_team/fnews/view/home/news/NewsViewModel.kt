package com.poly_team.fnews.view.home.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.poly_team.fnews.data.model.Media
import com.poly_team.fnews.data.network.NEWS
import com.poly_team.fnews.data.repository.AuthRepository
import com.poly_team.fnews.data.repository.NewsRepository
import com.poly_team.fnews.view.BaseViewModel
import com.poly_team.fnews.view.auth.register.RegisterViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val mApp: Application,
    private val mNewsRepository: NewsRepository
) : BaseViewModel() {

    private val TAG = "NewsViewModel"

    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            Log.e(TAG, t.message!!)
        }
    }

    var mMediaList: HashMap<String, Flow<PagingData<Media>>> = HashMap()
    private set


    fun getMediaList(field: String) {
        mMediaList[field] = mNewsRepository.getNewsByField(field).cachedIn(viewModelScope)
    }

    @Suppress("UNCHECKED_CAST")
    class NewsViewModelFactory(
        private var mApp: Application,
        private val mNewsRepository: NewsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(mApp, mNewsRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }

}