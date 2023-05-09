package com.poly_team.fnews.view.home.news.news_detail

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.poly_team.fnews.data.model.Comment
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.data.repository.NewsRepository
import com.poly_team.fnews.utility.getToken
import com.poly_team.fnews.view.BaseViewModel
import com.poly_team.fnews.view.home.*
import com.poly_team.fnews.view.home.ReadNewsService.Companion.CURRENT_ITEM
import com.poly_team.fnews.view.home.ReadNewsService.Companion.START_READ_NEWS_SERVICE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NewsContentViewModel @Inject constructor(
    private val mApp: Application,
    private val mNewsRepository: NewsRepository,
) : BaseViewModel() {

    private val TAG = "NewsContentViewModel"

    override var mHandleException = CoroutineExceptionHandler { _, t ->
        run {
            Log.e(TAG, t.message!!)
        }
    }

    private val mRelativeNews = MutableLiveData<List<News>>()
    val _mRelativeNews = mRelativeNews

    private val mCurrentNews = MutableLiveData<News>()
    val _mCurrentNews: LiveData<News> = mCurrentNews

    private val mCurrentNewsComment = MutableLiveData<List<Comment>>()
    val _mCurrentNewsComment: LiveData<List<Comment>> = mCurrentNewsComment

    fun setCurrentNews(news: News?) {
        news?.let { it ->
            mCurrentNews.value = it
            mNewsRepository.deleteNewsExcept(news.id)
            mNewsRepository.saveNewsToDatabase(news)
            viewModelScope.launch {
                news.field?.let {
                    val newsList = mNewsRepository.getNewsByField(news.field!!, 10)
                    setRelativeNews(newsList as ArrayList<News>)
                }
            }
            getCommentOfNews(0)
        }
    }

    fun getCommentOfNews(targetType: Int) {
        viewModelScope.launch {
            mCurrentNews.value?.id?.let {
                mCurrentNewsComment.value = mNewsRepository.getCommentOfNews(targetType, it)
            }
        }
    }

    fun speakText() {
        val intent = Intent(mApp, ReadNewsService::class.java)
        intent.action = START_READ_NEWS_SERVICE
        val newsId = mCurrentNews.value?.id
        newsId?.let {
            intent.putExtra(CURRENT_ITEM, it)
            mApp.startService(intent)
        }
    }

    private fun setRelativeNews(news: ArrayList<News>) {
        news.remove(mCurrentNews.value)
        mRelativeNews.value = news
        mNewsRepository.saveNewsToDatabase(news)
    }

    fun sendComment(commentContent: String, targetType: Int) {
        val token = getToken(mApp)
        if(token.isNullOrBlank()) {
            mEvent.value = NO_AUTH_EVENT
        } else {
            mCurrentNews.value?.id?.let {target ->
                viewModelScope.launch {
                    val success = mNewsRepository.sendComment(commentContent, target, targetType, token)
                    if(success) {
                        mEvent.value = COMMENT_SUCCESSFULLY
                    } else {
                        mEvent.value = COMMENT_FAILURE
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class NewsContentViewModelFactory(
        private var mApp: Application,
        private val mNewsRepository: NewsRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NewsContentViewModel::class.java)) {
                return NewsContentViewModel(mApp, mNewsRepository) as T
            }
            throw Exception("Unable construct view_model")
        }
    }


}