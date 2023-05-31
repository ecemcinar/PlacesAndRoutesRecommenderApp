package com.wheretogo.placesandroutesrecommenderapp.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.databinding.ItemCheckInBinding
import com.wheretogo.placesandroutesrecommenderapp.model.CheckIn

class CheckInAdapter : RecyclerView.Adapter<CheckInAdapter.CheckInItemHolder>() {

    private var list = emptyList<CheckIn>()

    class CheckInItemHolder(private val binding: ItemCheckInBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(checkIn: CheckIn) {
            binding.checkin = checkIn
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CheckInItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCheckInBinding.inflate(layoutInflater, parent, false)
                return CheckInItemHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckInItemHolder {
        return CheckInItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CheckInItemHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setCheckInList(checkIn: List<CheckIn>) {
        this.list = checkIn
        notifyDataSetChanged()
    }
}