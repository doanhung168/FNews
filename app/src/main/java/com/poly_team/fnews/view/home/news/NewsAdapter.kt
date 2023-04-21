package com.poly_team.fnews.view.home.news

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.poly_team.fnews.data.model.Media
import com.poly_team.fnews.databinding.ItemNewsBinding

class NewsAdapter : PagingDataAdapter<Media, NewsViewHolder>(COMPARATOR) {
    private val TAG = "NewsAdapter"

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.create(parent)



}

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view: ViewGroup): NewsViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = ItemNewsBinding.inflate(inflater, view, false)
            return NewsViewHolder(binding)
        }
    }

    fun bind(news: Media) {
        binding.news = news
        binding.executePendingBindings()
    }
}