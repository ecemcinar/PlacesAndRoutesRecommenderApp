package com.wheretogo.placesandroutesrecommenderapp.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.FeedItemBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Post

class FeedAdapter: RecyclerView.Adapter<FeedAdapter.FeedItemHolder>() {

    var postList = emptyList<Post>()

    class FeedItemHolder(private val binding: FeedItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.post = post
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FeedItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedItemBinding.inflate(layoutInflater, parent, false)
                return FeedItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemHolder {
        return FeedItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FeedItemHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun setFeedData(post: List<Post>) {
        this.postList = post
        notifyDataSetChanged()
    }
}