package com.wheretogo.placesandroutesrecommenderapp.ui.feed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentFeedBinding
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() =_binding!!
    private val adapter: FeedAdapter by lazy { FeedAdapter() }
    private val args by navArgs<FeedFragmentArgs>()
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFeedBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setUpRecyclerView()
        viewModel.getPostList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // disables the back arrow
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        lifecycleScope.launch {
            viewModel.getPostListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        adapter.setFeedData(it.result)
                        if (it.result.isEmpty()) {
                            binding.warningMessageTextView.apply {
                                text = getString(R.string.feed_no_post_text)
                                visibility = View.VISIBLE
                            }
                        }
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }
}