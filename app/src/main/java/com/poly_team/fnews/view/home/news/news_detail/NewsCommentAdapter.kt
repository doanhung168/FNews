package com.poly_team.fnews.view.home.news.news_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.poly_team.fnews.data.model.Comment
import com.poly_team.fnews.data.model.News
import com.poly_team.fnews.databinding.ItemCommentBinding
import com.poly_team.fnews.databinding.ItemRelativeNewsBinding

class NewsCommentAdapter : ListAdapter<Comment, CommentViewHolder>(COMPARATOR) {

    private val TAG = "NewsCommentAdapter"

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem

        }
    }

    private lateinit var mOnClickItem: (comment: Comment) -> Unit

    fun setListener(onClickItem: (comment: Comment) -> Unit) {
        mOnClickItem = onClickItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder.create(parent, mOnClickItem)

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: ")
        getItem(position)?.let {
            holder.bind(it)
        }
    }

}

class CommentViewHolder(
    private val binding: ItemCommentBinding,
    private val onClickItem: (comment: Comment) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(view: ViewGroup, onClickItem: (comment: Comment) -> Unit): CommentViewHolder {
            val inflater = LayoutInflater.from(view.context)
            val binding = ItemCommentBinding.inflate(inflater, view, false)
            return CommentViewHolder(binding, onClickItem)
        }
    }

    fun bind(comment: Comment) {
        binding.comment = comment
        binding.root.setOnClickListener { onClickItem(comment) }
        binding.executePendingBindings()
    }
}