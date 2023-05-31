package com.wheretogo.placesandroutesrecommenderapp.ui.routerecommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.RecommendationImageItemBinding

class RecommendationImageAdapter: RecyclerView.Adapter<RecommendationImageAdapter.ImageItemHolder>() {

    var recommendationList = emptyList<String>()

    class ImageItemHolder(private val binding: RecommendationImageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendationImage: String) {
            binding.recommendationImage = recommendationImage
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ImageItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecommendationImageItemBinding.inflate(layoutInflater, parent, false)
                return ImageItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemHolder {
        return ImageItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ImageItemHolder, position: Int) {
        val currentItem = recommendationList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return recommendationList.size
    }

    fun setRecommendationImageList(recommendation: List<String>) {
        this.recommendationList = recommendation
        notifyDataSetChanged()
    }
}