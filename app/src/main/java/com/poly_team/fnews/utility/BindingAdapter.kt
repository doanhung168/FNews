package com.poly_team.fnews.utility

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.poly_team.fnews.data.network.BASE_URL

@BindingAdapter("imageUrl", "error")
fun loadImage(view: ImageView, url: String, error: Drawable) {
    Glide.with(view).load(BASE_URL + url).error(error).into(view)
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

@BindingAdapter("time")
fun setTimeText(textView: TextView, time: Long) {
    textView.text = getTimeString(textView.context, time)
}