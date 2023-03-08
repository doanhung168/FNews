package com.poly_team.fnews.utility

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class FacebookComponent @Inject constructor(
    val mFacebookCallbackManager: com.facebook.CallbackManager,
    private val mLoginManager: LoginManager
) {

    companion object {
        private const val TAG = "FacebookComponent"
    }

    private var mFacebookLoginCompCallback: FacebookLoginCompCallback? = null

    init {
        mLoginManager.registerCallback(
            mFacebookCallbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleLoginResult(result)
                }

                override fun onCancel() {
                    mFacebookLoginCompCallback?.onCancel()
                }

                override fun onError(error: FacebookException) {
                    mFacebookLoginCompCallback?.onError(error)
                }

            })
    }


    private fun handleLoginResult(loginResult: LoginResult) {
        val accessToken = loginResult.accessToken.token
        Log.d(TAG, "Access token: $accessToken")
        val request = GraphRequest.newMeRequest(
            loginResult.accessToken
        ) { `object`: JSONObject, _: GraphResponse? ->
            Log.d(TAG, "object from request with access token: $`object`")
            var id: String? = null
            var name: String? = null
            var avatar: String? = null
            try {
                id = `object`.getString("id")
                name = `object`.getString("name")
                avatar = `object`.getJSONObject("picture").getJSONObject("data")["url"] as String
            } catch (e: JSONException) {
                Log.e(TAG, "Error get information: " + e.message)
            }
            mFacebookLoginCompCallback?.onSuccess(id, name, avatar)
            mLoginManager.logOut()
        }
        val parameters = Bundle()
        parameters.putString("fields", "id, name, picture.getType(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    fun login(activity: Activity?, facebookLoginCompCallback: FacebookLoginCompCallback?) {
        mFacebookLoginCompCallback = facebookLoginCompCallback
        mLoginManager.logInWithReadPermissions(activity, listOf("public_profile"))
    }
}

interface FacebookLoginCompCallback {
    fun onCancel()
    fun onError(error: FacebookException)
    fun onSuccess(id: String?, name: String?, avatar: String?)
}