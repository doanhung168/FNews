package com.poly_team.fnews.utility

import android.app.Activity
import android.app.Application
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.*
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


fun String.isNotValidUsername(): Boolean {
    return this.length < 6
}

//Minimum eight characters, at least one letter and one number
fun String.isNotValidPassword(): Boolean {
    val result =
        this.matches(Regex("^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,}$"))
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


@BindingAdapter("errorText")
fun setErrorText(textView: TextView, errorText: String) {
    if (errorText.isEmpty()) {
        textView.visibility = View.GONE
    } else {
        textView.visibility = View.VISIBLE
    }
    textView.text = errorText
}

