package com.poly_team.fnews.view.home.news.news_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.databinding.ItemNewsBinding
import com.poly_team.fnews.databinding.ItemRelativeNewsBinding

class RelativeNewsAdapter : ListAdapter<News, NewsViewHolder>(COMPARATOR) {

    private val TAG = "RelativeNewsAdapter"

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean =
                oldItem == newItem
        }
    }

    private lateinit var mOnClickItem: (news: News) -> Unit

    fun setListener(onClickItem: (news: News) -> Unit) {
        mOnClickItem = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder.create(parent, mOnClickItem)


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        getItem(position)?.let {
            holder.bind(it)
        }
    }


}

class NewsViewHolder(
    private val binding: ItemRelativeNewsBinding,
    private val onClickItem: (news: News) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view: ViewGroup, onClickItem: (news: News) -> Unit): NewsViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = ItemRelativeNewsBinding.inflate(inflater, view, false)
            return NewsViewHolder(binding, onClickItem)
        }
    }

    fun bind(news: News) {
        binding.news = news
        binding.root.setOnClickListener { onClickItem(news) }
        binding.executePendingBindings()
    }
}