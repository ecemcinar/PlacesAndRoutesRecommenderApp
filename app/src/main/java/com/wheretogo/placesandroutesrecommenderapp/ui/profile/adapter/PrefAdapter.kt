package com.wheretogo.placesandroutesrecommenderapp.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.ProfilePrefItemBinding

class PrefAdapter : RecyclerView.Adapter<PrefAdapter.PrefItemHolder>() {

    private var list = emptyList<String>()

    class PrefItemHolder(private val binding: ProfilePrefItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(pref: String) {
            binding.preference = pref
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PrefItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ProfilePrefItemBinding.inflate(layoutInflater, parent, false)
                return PrefItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrefItemHolder {
        return PrefItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PrefItemHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setPrefList(prefList: List<String>) {
        this.list = prefList
        notifyDataSetChanged()
    }
}