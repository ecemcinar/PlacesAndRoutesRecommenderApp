package com.wheretogo.placesandroutesrecommenderapp.ui.postview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wheretogo.placesandroutesrecommenderapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostViewFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_view, container, false)
    }
}