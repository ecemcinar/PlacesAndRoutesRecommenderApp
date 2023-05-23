package com.wheretogo.placesandroutesrecommenderapp.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.ItemPostBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Post

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostItemHolder>() {

    private var postList = emptyList<Post>()

    class PostItemHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(post: Post) {
                binding.post = post
                binding.executePendingBindings()
            }

        companion object {
            fun from(parent: ViewGroup): PostItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemPostBinding.inflate(layoutInflater, parent, false)
                return PostItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostItemHolder {
        return PostItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PostItemHolder, position: Int) {
        val currentItem = postList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun setPostList(post: List<Post>) {
        this.postList = post
        notifyDataSetChanged()
    }
}