package com.poly_team.fnews.view.home.news

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.poly_team.fnews.data.model.Field
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.data.repository.FieldRepository
import com.poly_team.fnews.data.repository.NewsRepository
import com.poly_team.fnews.view.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    mApp: Application,
    private val mNewsRepository: NewsRepository,
    mFieldRepository: FieldRepository
) : BaseViewModel() {

    private val TAG = "NewsViewModel"

    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            Log.e(TAG, t.message!!)
        }
    }

    private val mFieldList = MutableLiveData<List<Field>>()
    val _mFieldList: LiveData<List<Field>> = mFieldList

    var mNewsList: HashMap<String, Flow<PagingData<News>>> = HashMap()
    private set

    init {
        viewModelScope.launch {
            val fieldList = mFieldRepository.getField()
            mFieldList.value = fieldList
            fieldList.forEach {field ->
                mNewsList[field.id as String] = mNewsRepository.getNewsByField(field.id as String)
            }
        }
    }

    fun loadData(fieldId: String) {
        mNewsList[fieldId] = mNewsRepository.getNewsByField(fieldId)
    }

    @Suppress("UNCHECKED_CAST")
    class NewsViewModelFactory(
        private var mApp: Application,
        private val mNewsRepository: NewsRepository,
        private val mFieldRepository: FieldRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsViewModel::class.java)) {
                return NewsViewModel(mApp, mNewsRepository, mFieldRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }

}