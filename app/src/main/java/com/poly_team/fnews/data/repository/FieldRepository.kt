package com.poly_team.fnews.data.repository

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.poly_team.fnews.data.model.Field
import com.poly_team.fnews.data.network.Network
import com.poly_team.fnews.data.network.NetworkResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class FieldRepository @Inject constructor(
    private val mApp: Application,
    private val mNetwork: Network,
    private val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getField() : List<Field>  = withContext(mDispatcher) {
        return@withContext passerField(mNetwork.getField())
    }

    private fun passerField(networkResponse: NetworkResponse): ArrayList<Field> {
        val result = ArrayList<Field>()
        val jsonArray = JSONArray(Gson().toJson(networkResponse.data))
        for(i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val field = Field()
            field.id = jsonObject.getString("_id")
            field.value = jsonObject.getString("value")
            field.description = jsonObject.getString("description")
            result.add(field)
        }
        return result
    }



}