package com.wheretogo.placesandroutesrecommenderapp.ui.routerecommendation

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentRouteRecommendationBinding
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute.CreateRouteFragment
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

@AndroidEntryPoint
class RouteRecommendationFragment : Fragment() {

    private var _binding: FragmentRouteRecommendationBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RouteRecommendationFragmentArgs>()
    private val viewModel: RouteRecommendationViewModel by viewModels()
    private val adapter: RecommendationImageAdapter by lazy { RecommendationImageAdapter() }

    private lateinit var timer: Timer
    private lateinit var handler: Handler
    private var currentPosition: Int = 0
    private var listSize: Int? = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteRecommendationBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = viewLifecycleOwner
        args.recommendation?.documentId?.let { viewModel.getRecommendation(it) }
        binding.recommendation = args.recommendation
        setUpRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCollectors()
        binding.showRouteButton.setOnClickListener {
            val list = ArrayList(viewModel.locationList)
            val fragment = CreateRouteFragment()
            val bundle = Bundle()
            bundle.apply {
                this.putParcelableArrayList("placeList", list)
            }
            fragment.arguments = bundle
            findNavController().navigate(R.id.action_routeRecommendationFragment_to_createRouteFragment, bundle)
        }

        timer = Timer()
        handler = Handler()
        val delay: Long = 5000
        val period: Long = 5000

        timer.schedule(object: TimerTask() {
            override fun run() {
                handler.post {
                    currentPosition = (currentPosition + 1) % listSize!!
                    binding.imageRecyclerView.smoothScrollToPosition(currentPosition)
                }
            }

        }, delay, period)
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            viewModel.getRecommendationFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        binding.progressBarLoading.visibility = View.GONE
                        binding.recommendation = it.result
                        it.result.imageList?.let { list ->
                            adapter.setRecommendationImageList(list)
                        }
                        it.result.placeList?.let { it1 -> viewModel.setPlaceList(it1) }
                        listSize = it.result.placeList?.size
                    }
                    is Resource.Loading -> {
                        binding.progressBarLoading.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpRecyclerView() {
        binding.imageRecyclerView.adapter = adapter
        binding.imageRecyclerView.layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
    }
}