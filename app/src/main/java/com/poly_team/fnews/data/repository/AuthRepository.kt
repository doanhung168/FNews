package com.poly_team.fnews.data.repository

import android.app.Application
import com.poly_team.fnews.data.Network
import com.poly_team.fnews.data.NetworkResponse
import com.poly_team.fnews.utility.saveToken
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AuthRepository @Inject constructor(
    private val mApp: Application,
    private val mNetwork: Network,
    private val mDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val TAG = "AuthRepository"
        private const val AUTH_METHOD = "Bearer "
        private const val AUTH_KEY = "Authorization"
        const val FACEBOOK_ACCOUNT = "facebook"
        const val GOOGLE_ACCOUNT = "google"
    }

    suspend fun login(username: String, password: String): Boolean =
        withContext(mDispatcher) {
            val response = mNetwork.login(username, password)
            val result = response.body()!!.success
            if (result) {
                saveToken(mApp, response.headers()[AUTH_KEY]!!)
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
        withContext(mDispatcher) {
            val response = mNetwork.loginWith(accountType, accountId, displayName, avatar, email)
            val result = response.body()!!.success
            if (result) {
                saveToken(mApp, response.headers()[AUTH_KEY]!!)
            }
            result
        }

    suspend fun register(username: String, password: String, email: String): NetworkResponse =
        withContext(mDispatcher) {
            return@withContext mNetwork.register(username, password, email)
        }

    suspend fun findPassword(email: String) =
        withContext(mDispatcher) {
            return@withContext mNetwork.findPassword(email)
        }

    suspend fun autoLogin(token: String) =
        withContext(mDispatcher) {
            return@withContext mNetwork.autoLogin(AUTH_METHOD + token)
        }

}