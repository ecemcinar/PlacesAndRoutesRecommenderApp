package com.wheretogo.placesandroutesrecommenderapp.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.RecommendationItemBinding
import com.wheretogo.placesandroutesrecommenderapp.model.Recommendation

class RecommendationAdapter: RecyclerView.Adapter<RecommendationAdapter.RecommendationItemHolder>() {

    var list = emptyList<Recommendation>()

    class RecommendationItemHolder(private val binding: RecommendationItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendation: Recommendation)  {
            binding.recommendation = recommendation
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecommendationItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecommendationItemBinding.inflate(layoutInflater, parent, false)
                return RecommendationItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationItemHolder {
        return RecommendationItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecommendationItemHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setRecommendationData(recommendation: List<Recommendation>) {
        this.list = recommendation
        notifyDataSetChanged()
    }
}