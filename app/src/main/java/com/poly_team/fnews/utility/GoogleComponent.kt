package com.poly_team.fnews.utility

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import javax.inject.Inject

class GoogleComponent @Inject constructor(
    private val mGoogleSignInClient: GoogleSignInClient
) {

    companion object {
        const val REQUEST_CODE = 1
    }

    var mCallbackManager: CallbackManager? = null
        private set

    fun login(activity: Activity, googleComponentCallback: GoogleComponentCallback) {
        mCallbackManager = CallbackManager(googleComponentCallback)
        activity.startActivityForResult(
            mGoogleSignInClient.signInIntent,
            REQUEST_CODE
        )
    }


}

class CallbackManager(
    private val mGoogleComponentCallback: GoogleComponentCallback
) {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleComponent.REQUEST_CODE) {
            if (data == null) {
                mGoogleComponentCallback.onError(Exception("GG Data is null"))
            } else {
                handleSignInResult(data)
            }
        }
    }

    private fun handleSignInResult(intent: Intent) {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
            val account = task.getResult(ApiException::class.java)
            mGoogleComponentCallback.onSuccess(
                account.id,
                account.displayName,
                account.photoUrl.toString(),
                account.email
            )
        } catch (e: Exception) {
            mGoogleComponentCallback.onError(e)
        }
    }
}

interface GoogleComponentCallback {
    fun onError(error: Exception)
    fun onSuccess(id: String?, displayName: String?, avatar: String?, email: String?)
}