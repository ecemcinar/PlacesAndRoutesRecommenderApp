package com.wheretogo.placesandroutesrecommenderapp.ui.maps.checkin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.CategoryItemBinding
import com.wheretogo.placesandroutesrecommenderapp.extension.isNotNullAndTrue

class CategoryItemAdapter(
    private var categoryList: List<CategoryModel?>?,
    private var listener: (CategoryModel) -> Unit
): RecyclerView.Adapter<CategoryItemAdapter.CategoryItemHolder>() {

    private lateinit var context: Context

    class CategoryItemHolder(val binding: CategoryItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(category: CategoryModel) {
                binding.model = category
                binding.executePendingBindings()
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(layoutInflater, parent, false)
        context = parent.context
        return CategoryItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryItemHolder, position: Int) {
        var currentItem: CategoryModel? = null
        categoryList?.get(position)?.let {
            currentItem = it
        }
        holder.binding.prefContainer.setOnClickListener {
            currentItem?.isSelected = currentItem?.isSelected?.not()
            currentItem?.let { data -> listener.invoke(data) }
            it.background = if (currentItem?.isSelected.isNotNullAndTrue())  context.getDrawable(
                R.drawable.item_selected_background
            ) else context.getDrawable(R.drawable.item_background)
        }
        currentItem?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        categoryList?.let {
            return it.size
        }
        return 0
    }

    fun setData(data: List<CategoryModel?>) {
        this.categoryList = data
        notifyDataSetChanged()
    }
}