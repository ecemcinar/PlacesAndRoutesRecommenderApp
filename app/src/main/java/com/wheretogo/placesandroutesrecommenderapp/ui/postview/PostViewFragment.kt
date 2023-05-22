package com.wheretogo.placesandroutesrecommenderapp.ui.postview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentPostViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostViewFragment: Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<PostViewFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPostViewBinding.inflate(
            inflater,
            container,
            false
        )
        binding.post = args.post
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}