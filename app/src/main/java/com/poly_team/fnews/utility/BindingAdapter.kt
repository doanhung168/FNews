package com.poly_team.fnews.utility

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.poly_team.fnews.data.model.News
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

@SuppressLint("SetJavaScriptEnabled")
@BindingAdapter("loadHtml")
fun setHtml(webView: WebView, news: News) {
    with(webView) {
        webChromeClient = WebChromeClient()
        settings.javaScriptEnabled = true
        settings.pluginState = WebSettings.PluginState.ON
        settings.pluginState = WebSettings.PluginState.ON_DEMAND
        settings.textZoom = (settings.textZoom * 1.2f).toInt()
        val data = "<h4 style='font-size: 25px;margin:0 0'>${news.title}</h4> <i>${getTimeString(webView.context, news.time)}</i> " + news.content
        loadData(data, "text/html", "UTF-8")
    }
}

