package com.poly_team.fnews.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.poly_team.fnews.data.model.Media
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException

const val STARTING_PAGE_INT = 0

class NewsPagingSource(
    private val mNetwork: Network,
    private val mField: String,
    private val mType: String
) : PagingSource<Int, Media>() {

    private val TAG = "NewsPagingSource"

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Media> {
        val page = params.key ?: STARTING_PAGE_INT
        val queryMap = makeAQueryMap(mType, mField, page, params.loadSize)
        val response = mNetwork.getMedia(queryMap)
        if(response.success) {
            Log.i(TAG, "load: success result")
            val data = Gson().toJson(response.data)
            return try {
                LoadResult.Page(
                    data = passerMedia(data),
                    prevKey = if (STARTING_PAGE_INT == 0) null else (page - 1),
                    nextKey = if(JSONObject(data).getJSONArray("medias").isNull(0)) null else (page + 1)
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

    private fun passerMedia(data: String): List<Media> {
        Log.i(TAG, "passerMedia: $data")
        val result = ArrayList<Media>()
        val jsonObject = JSONObject(data)
        val jsonArray = jsonObject.getJSONArray("medias")
        for (index in 0 until jsonArray.length()) {
            val media = Media()
            val jObject = jsonArray.getJSONObject(index)
            media.id = jObject.getString("_id")
            media.avatar = jObject.getString("avatar")
            media.title = jObject.getString("title")
            media.content = jObject.getString("content")
            media.active = jObject.getBoolean("active")
            media.state = jObject.getString("state")
            media.author = jObject.getString("author")
            media.field = jObject.getString("field")
            media.type = jObject.getString("type")
            media.like = jObject.getInt("like")
            media.disLike = jObject.getInt("dislike")
            media.time = jObject.getLong("time")

            val tags = ArrayList<String>()
            val jsonArrayTags = jObject.getJSONArray("tags")
            for (i in 0 until jsonArrayTags.length()) {
                tags.add(jsonArrayTags.getString(i))
            }
            result.add(media)
        }
        return result
    }

    override fun getRefreshKey(state: PagingState<Int, Media>): Int? {
        return state.anchorPosition
    }

}