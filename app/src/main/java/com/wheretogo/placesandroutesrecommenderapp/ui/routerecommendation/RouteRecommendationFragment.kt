package com.wheretogo.placesandroutesrecommenderapp.ui.routerecommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentRouteRecommendationBinding
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute.CreateRouteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RouteRecommendationFragment : Fragment() {

    private var _binding: FragmentRouteRecommendationBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<RouteRecommendationFragmentArgs>()

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
        binding.recommendation = args.recommendation

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.showRouteButton.setOnClickListener {
            val fragment = CreateRouteFragment()
            val bundle = Bundle()
            bundle.apply {
                this.putParcelableArrayList("placeList",
                    args.recommendation?.placeList?.let { it1 -> ArrayList(it1) })
            }
            fragment.arguments = bundle
            findNavController().navigate(R.id.action_routeRecommendationFragment_to_createRouteFragment, bundle)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}