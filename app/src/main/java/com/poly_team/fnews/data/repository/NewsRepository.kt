package com.poly_team.fnews.data.repository

import android.app.Application
import android.util.JsonToken
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.poly_team.fnews.data.local.AppDatabase
import com.poly_team.fnews.data.model.Comment
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.data.network.NEWS
import com.poly_team.fnews.data.network.Network
import com.poly_team.fnews.data.network.NewsPagingSource
import com.poly_team.fnews.utility.getToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

private const val PAGE_SIZE = 10
const val AUTH_METHOD = "Bearer "
class NewsRepository @Inject constructor(
    private val mAppDatabase: AppDatabase,
    private val mNetwork: Network,
    private val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val TAG = "NewsRepository"

    fun getNewsByField(field: String): Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE),
            pagingSourceFactory = { NewsPagingSource(mNetwork, field, NEWS) }
        ).flow

    suspend fun getNewsByField(field: String, limit: Int): List<News> = withContext(mDispatcher) {
        val queryMap = HashMap<String, Any>()
        queryMap["field"] = field
        queryMap["page"] = 0
        queryMap["per_page"] = limit
        val res = mNetwork.getMedia(queryMap)
        if (res.success) {
            val data = Gson().toJson(res.data)
            return@withContext passerMedia(data)
        } else {
            return@withContext emptyList<News>()
        }
    }

    suspend fun getCommentOfNews(targetType: Int, target: String): ArrayList<Comment> =
        withContext(mDispatcher) {
            val res = mNetwork.getCommentOfNews(targetType, target)
            if (res.success) {
                val data = Gson().toJson(res.data)
                Log.i(TAG, "getCommentOfNews: $data")
                return@withContext parserComment(data)
            } else {
                return@withContext arrayListOf()
            }
        }


    suspend fun sendComment(content: String, target: String, targetType: Int, token: String) =
        withContext(mDispatcher) {
            Log.i(TAG, "sendComment: ")
            val res = mNetwork.sendComment(content, target, targetType, AUTH_METHOD + token)
            Log.i(TAG, "sendComment: ${res.success}")
            return@withContext res.success
        }

    fun saveNewsToDatabase(news: News) {
        mAppDatabase.newsDao().insert(news)
    }

    fun saveNewsToDatabase(news: ArrayList<News>) {
        mAppDatabase.newsDao().insertAll(news)
    }

    fun deleteNewsExcept(newsId: String) {
        mAppDatabase.newsDao().deleteAllExpect(newsId)
    }


    private fun passerMedia(data: String): List<News> {
        Log.i(TAG, "passerMedia: $data")
        val result = ArrayList<News>()
        val jsonObject = JSONObject(data)
        val jsonArray = jsonObject.getJSONArray("news")
        for (index in 0 until jsonArray.length()) {
            val news = News()
            val jObject = jsonArray.getJSONObject(index)
            news.id = jObject.getString("_id")
            news.avatar = jObject.getString("avatar")
            news.title = jObject.getString("title")
            news.content = jObject.getString("content")
            news.active = jObject.getBoolean("active")
            news.state = jObject.getString("state")

            if (jObject.has("stateExtra")) {
                news.stateExtra = jObject.getString("stateExtra")
            }
            news.author = jObject.getString("author")
            news.field = jObject.getString("field")
            news.like = jObject.getInt("like")

            if (jObject.has("view")) {
                news.view = jObject.getLong("view")
            }

            if (jObject.has("time")) {
                news.time = jObject.getLong("time")
            }
            val tags = ArrayList<String>()
            val jsonArrayTags = jObject.getJSONArray("tags")
            for (i in 0 until jsonArrayTags.length()) {
                tags.add(jsonArrayTags.getString(i))
            }
            result.add(news)
        }
        return result
    }

    private fun parserComment(data: String): ArrayList<Comment> {
        val result = ArrayList<Comment>()
        val jsonArray = JSONArray(data)
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            val comment = Comment()
            comment.id = jsonObject.getString("_id")
            comment.content = jsonObject.getString("content")
            comment.like = jsonObject.getLong("like")
            comment.time = jsonObject.getLong("time")

            val ownerObj = jsonObject.getJSONObject("owner")
            comment.ownerId = ownerObj.getString("_id")
            comment.ownerName = ownerObj.getString("display_name")

            if (ownerObj.has("avatar")) {
                comment.ownerAvatar = ownerObj.getString("avatar")
            }

            if (jsonObject.has("childrent_comment")) {
                val childrenCommentObj = jsonObject.getJSONArray("childrent_comment")
                val childrenComment = ArrayList<String>()
                for (i in 0 until childrenCommentObj.length()) {
                    childrenComment.add(childrenCommentObj.getString(i))
                }
                comment.childComment = childrenComment
            }
            result.add(comment)
        }
        return result
    }


}