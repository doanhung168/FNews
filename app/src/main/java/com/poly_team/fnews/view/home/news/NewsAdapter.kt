package com.poly_team.fnews.view.home.news

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
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

    private lateinit var mOnClickItem: (media: Media) -> Unit

    fun setListener(onClickItem: (media: Media) -> Unit) {
        mOnClickItem = onClickItem
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NewsViewHolder.create(parent, mOnClickItem)


}

class NewsViewHolder(
    private val binding: ItemNewsBinding,
    private val onClickItem: (media: Media) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view: ViewGroup, onClickItem: (media: Media) -> Unit): NewsViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = ItemNewsBinding.inflate(inflater, view, false)
            return NewsViewHolder(binding, onClickItem)
        }
    }

    fun bind(news: Media) {
        binding.news = news
        binding.root.setOnClickListener {onClickItem(news)}
        binding.executePendingBindings()
    }
}