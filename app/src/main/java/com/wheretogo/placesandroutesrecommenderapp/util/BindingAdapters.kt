package com.wheretogo.placesandroutesrecommenderapp.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter

class BindingAdapters {

    companion object {
        @BindingAdapter("android:setSrc")
        @JvmStatic
        fun setImageViewResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }
}