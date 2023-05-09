package com.poly_team.fnews.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poly_team.fnews.data.model.News
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

const val STARTING_PAGE_INT = 0

class NewsPagingSource(
    private val mNetwork: Network,
    private val mField: String,
    private val mType: String
) : PagingSource<Int, News>() {

    private val TAG = "NewsPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val page = params.key ?: STARTING_PAGE_INT
        val queryMap = makeAQueryMap(mField, page, params.loadSize)
        val response = mNetwork.getMedia(queryMap)
        if(response.success) {
            Log.i(TAG, "load: success result")
            val data = Gson().toJson(response.data)
            return try {
                LoadResult.Page(
                    data = passerMedia(data),
                    prevKey = if (STARTING_PAGE_INT == 0) null else (page - 1),
                    nextKey = if(JSONObject(data).getJSONArray("news").isNull(0)) null else (page + 1)
                )
            } catch (e: IOException) {
                Log.e(TAG, "load IOEx: ${e.message}")
                return LoadResult.Error(e)
            } catch (e: HttpException) {
                Log.e(TAG, "load HttpEx: ${e.message}")
                return LoadResult.Error(e)
            } catch (e: Exception) {
                Log.e(TAG, "load Ex: ${e.message}")
                return LoadResult.Error(e)
            }
        } else {
            Log.w(TAG, "load: fail result")
            return LoadResult.Error(Exception("No resource"))
        }
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

            if(jObject.has("stateExtra")) {
                news.stateExtra = jObject.getString("stateExtra")
            }
            news.author = jObject.getString("author")
            news.field = jObject.getString("field")
            news.like = jObject.getInt("like")

            if(jObject.has("view")) {
                news.view = jObject.getLong("view")
            }

            if(jObject.has("time")) {
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

    override fun getRefreshKey(state: PagingState<Int, News>): Int? {
        return state.anchorPosition
    }

    private fun makeAQueryMap( field: String, page: Int, perPage: Int): Map<String, Any> {
        val queryMap = HashMap<String, Any>()
        queryMap["field"] = field
        queryMap["page"] = page
        queryMap["per_page"] = perPage
        return queryMap
    }

}