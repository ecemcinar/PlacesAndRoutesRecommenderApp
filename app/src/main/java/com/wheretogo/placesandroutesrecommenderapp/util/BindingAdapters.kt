package com.wheretogo.placesandroutesrecommenderapp.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation

class BindingAdapters {

    companion object {
        @BindingAdapter("android:setSrc")
        @JvmStatic
        fun setImageViewResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }
}

@BindingAdapter("android:downloadUrl") // xml'de bu fonksiyonu calistirmamak icin
fun downloadImage(view: ImageView, url: String?) {
    view.downloadFromUrl(
        url, placeholderProgressBar(view.context),
        CircleCropTransformation()
    )
}

@BindingAdapter("android:downloadUrlRounded") // xml'de bu fonksiyonu calistirmamak icin
fun downloadImageRounded(view: ImageView, url: String?) {
    view.downloadFromUrl(
        url, placeholderProgressBar(view.context),
        RoundedCornersTransformation(topLeft = 20f, topRight = 20f, bottomLeft = 20f, bottomRight = 20f)
    )
}

fun placeholderProgressBar(context: Context): CircularProgressDrawable {

    return CircularProgressDrawable(context).apply {
        strokeWidth = 8f
        centerRadius = 40f
        start()
    }
}

fun ImageView.downloadFromUrl(
    url: String?,
    circularProgressDrawable: CircularProgressDrawable,
    transformation: Transformation
) {

    load(url) {
        crossfade(true)
        placeholder(circularProgressDrawable)
        // default olarak circle crop ve rounded cropa sahip
        transformations(transformation)
        //transformations(RoundedCornersTransformation())
    }
}