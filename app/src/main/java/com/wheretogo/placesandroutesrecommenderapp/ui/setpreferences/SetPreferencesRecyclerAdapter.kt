package com.wheretogo.placesandroutesrecommenderapp.ui.setpreferences

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.SetPreferencesItemBinding
import com.wheretogo.placesandroutesrecommenderapp.extension.isNotNullAndTrue

class SetPreferencesRecyclerAdapter(
    private var data: List<SetPreferencesModel?>?,
    private var listener: (SetPreferencesModel) -> Unit
) : RecyclerView.Adapter<SetPreferencesRecyclerAdapter.SetPreferencesItemHolder>() {

    private lateinit var context: Context

    class SetPreferencesItemHolder(val binding: SetPreferencesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: SetPreferencesModel?) {
            binding.model = model
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetPreferencesItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SetPreferencesItemBinding.inflate(layoutInflater, parent, false)
        context = parent.context
        return SetPreferencesItemHolder(binding)
    }

    override fun onBindViewHolder(holder: SetPreferencesItemHolder, position: Int) {
        var currentItem: SetPreferencesModel? = null
        data?.get(position)?.let {
            currentItem = it
        }
        holder.binding.prefContainer.setOnClickListener {
            currentItem?.isSelected = currentItem?.isSelected?.not()
            currentItem?.let { data -> listener.invoke(data) }
            holder.binding.prefContainer.background = if (currentItem?.isSelected?.isNotNullAndTrue() == true) context.getDrawable(
                R.drawable.item_selected_background) else context.getDrawable(R.drawable.item_background)
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