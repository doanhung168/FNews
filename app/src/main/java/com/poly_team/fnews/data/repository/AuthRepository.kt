package com.poly_team.fnews.data.repository

import android.app.Application
import com.poly_team.fnews.data.Network
import com.poly_team.fnews.data.NetworkResponse
import com.poly_team.fnews.utility.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val mApp: Application,
    private val mNetwork: Network
) {

    companion object {
        private const val TAG = "AuthRepository"
        private const val AUTH_KEY = "Authorization"
        const val FACEBOOK_ACCOUNT = "facebook"
        const val GOOGLE_ACCOUNT = "google"
    }

    suspend fun login(username: String, password: String): Boolean =
        withContext(Dispatchers.IO) {
            val response = mNetwork.login(username, password)
            val result = response.body()!!.success
            if (result) {
                Token.save(mApp, response.headers()[AUTH_KEY]!!)
            }
            result
        }

    suspend fun loginWith(
        accountType: String,
        accountId: String,
        displayName: String,
        avatar: String,
        email: String?
    ) =
        withContext(Dispatchers.IO) {
            val response = mNetwork.loginWith(accountType, accountId, displayName, avatar, email)
            val result = response.body()!!.success
            if (result) {
                Token.save(mApp, response.headers()[AUTH_KEY]!!)
            }
            result
        }

    suspend fun register(username: String, password: String, email: String): NetworkResponse =
        withContext(Dispatchers.IO) {
            return@withContext mNetwork.register(username, password, email)
        }
}