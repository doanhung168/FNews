package com.poly_team.fnews.utility

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.*
import com.poly_team.fnews.R
import com.google.android.material.textfield.TextInputLayout

private const val DAY_DURATION = (1000 * 60 * 60 * 24).toLong()
private const val HOUR_DURATION = (1000 * 60 * 60).toLong()
private const val MINUTE_DURATION = (1000 * 60).toLong()
fun String.isNotValidUsername(): Boolean {
    return this.length < 6
}

//Minimum eight characters, at least one letter and one number
fun String.isNotValidPassword(): Boolean {
    val result =
        this.matches(Regex("^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#&]).{8,30}$"))
    return !result
}

fun String.isNotValidEmail(): Boolean {
    val result = this.matches(Regex("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$"))
    return !result
}

fun TextInputLayout?.text(): String {
    return this?.editText?.text.toString()
}

fun hideKeyboard(activity: Activity) {
    val imm: InputMethodManager =
        activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun makeFullscreen(activity: Activity) {
    WindowCompat.setDecorFitsSystemWindows(activity.window, false)
}


fun saveToken(app: Application, token: String) {
    val share = app.getSharedPreferences("app", Activity.MODE_PRIVATE)
    val edit = share.edit()
    edit.putString("token", token)
    edit.apply()
}

fun getToken(app: Application): String? {
    val share = app.getSharedPreferences("app", Activity.MODE_PRIVATE)
    return share.getString("token", null)
}

fun saveFirstRun(app: Application) {
    val share = app.getSharedPreferences("app", Activity.MODE_PRIVATE)
    val edit = share.edit()
    edit.putBoolean("first_run", false)
    edit.apply()
}

fun getFirstRun(app: Application): Boolean {
    val share = app.getSharedPreferences("app", Activity.MODE_PRIVATE)
    return share.getBoolean("first_run", true)
}


fun getTimeString(context: Context, time: Long): String? {
    val numberTime = System.currentTimeMillis() - time
    if (numberTime < 0) {
        return ""
    }
    if (numberTime >= DAY_DURATION) {
        val day = (numberTime / DAY_DURATION).toInt()
        return context.getString(R.string.day_time_format, day)
    }
    if (numberTime >= HOUR_DURATION) {
        val hour = (numberTime / HOUR_DURATION).toInt()
        return context.getString(R.string.hour_time_format, hour)
    }
    val mm = (numberTime / MINUTE_DURATION).toInt()
    return if (mm == 0) {
        context.getString(R.string.minute_time_format, 1)
    } else {
        context.getString(R.string.minute_time_format, mm)
    }
}

