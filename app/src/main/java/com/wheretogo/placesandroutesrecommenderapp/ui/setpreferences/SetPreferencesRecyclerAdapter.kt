package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.SetPreferencesItemBinding

class SetPreferencesRecyclerAdapter(private var data: List<SetPreferencesModel?>?): RecyclerView.Adapter<SetPreferencesRecyclerAdapter.SetPreferencesItemHolder>() {
    class SetPreferencesItemHolder(val binding: SetPreferencesItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SetPreferencesModel?) {
            binding.model = model
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetPreferencesItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SetPreferencesItemBinding.inflate(layoutInflater, parent, false)
        return SetPreferencesItemHolder(binding)
    }

    override fun onBindViewHolder(holder: SetPreferencesItemHolder, position: Int) {
        var currentItem: SetPreferencesModel? = null
        data?.get(position)?.let {
            currentItem = it
        }
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        data?.let {
            return it.size
        }
        return 0
    }

    fun setData(data: List<SetPreferencesModel?>) {
        this.data = data
        notifyDataSetChanged()
    }
}