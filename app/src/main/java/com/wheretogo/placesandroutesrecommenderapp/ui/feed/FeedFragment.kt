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
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentFeedBinding
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() =_binding!!
    private val adapter: FeedAdapter by lazy { FeedAdapter() }
    private val recommendationAdapter: RecommendationAdapter by lazy { RecommendationAdapter() }
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
        viewModel.getRecommendationList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // disables the back arrow
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        lifecycleScope.launchWhenStarted {
            viewModel.getRecommendationListFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        recommendationAdapter.setRecommendationData(it.result)
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
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
        binding.postRecyclerView.adapter = adapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireActivity())

        binding.recommendationRecyclerView.adapter = recommendationAdapter
        binding.recommendationRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    }
}