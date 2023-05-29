package com.wheretogo.placesandroutesrecommenderapp.util

import android.content.Context
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.model.Post
import com.wheretogo.placesandroutesrecommenderapp.model.Recommendation
import com.wheretogo.placesandroutesrecommenderapp.ui.feed.FeedFragmentDirections
import com.wheretogo.placesandroutesrecommenderapp.ui.profile.ProfileFragmentDirections

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

@BindingAdapter("android:sendDataToFragment")
fun sendDataToFragment(view: ConstraintLayout, currentItem: Post) {
    view.setOnClickListener {
        val action = FeedFragmentDirections.actionFeedFragmentToPostViewFragment(currentItem)
        view.findNavController().navigate(action)
    }
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

@BindingAdapter("android:setPlaceCategoryImage")
fun setPlaceCategoryImage(view: ImageView, category: String?) {
    var image = when (category) {
        "SHOPPING" ->  R.drawable.shopping
        "BAKERY" -> R.drawable.bakery
        "CINEMA" -> R.drawable.cinema
        "MUSEUM" -> R.drawable.museum
        "NATURE" -> R.drawable.landscape
        "MUSIC" -> R.drawable.music
        "THEATRE" -> R.drawable.theatre
        "DINING" -> R.drawable.dining
        "FITNESS" -> R.drawable.dumbbell
        "COFFEE" -> R.drawable.coffee
        else -> { R.drawable.place }
    }
    view.setImageResource(image)
}

@BindingAdapter("android:sendDataToPostViewFragment")
fun sendDataToPostViewFragment(view: ConstraintLayout, currentItem: Post) {
    view.setOnClickListener {
        val action = ProfileFragmentDirections.actionProfileFragmentToPostViewFragment(currentItem)
        view.findNavController().navigate(action)
    }
}

@BindingAdapter("android:sendDataToRouteRecommendationFragment")
fun sendDataToRouteRecommendationFragment(view: ConstraintLayout, currentItem: Recommendation) {
    view.setOnClickListener {
        val action = FeedFragmentDirections.actionFeedFragmentToRouteRecommendationFragment(currentItem)
        view.findNavController().navigate(action)
    }
}