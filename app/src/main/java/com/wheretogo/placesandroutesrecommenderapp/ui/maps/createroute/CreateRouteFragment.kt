package com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCreateRouteBinding
import com.wheretogo.placesandroutesrecommenderapp.model.map.LatLng
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.MapsSharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateRouteFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private var _binding: FragmentCreateRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateRouteViewModel by viewModels()
    private val sharedViewModel: MapsSharedViewModel by viewModels()

    private var map: GoogleMap? = null
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRouteBinding.inflate(inflater, container, false)
        binding.apply {
            viewModel = viewModel
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                map = it
                map?.setOnMapLongClickListener(this@CreateRouteFragment)
            }
        }

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Construct a PlacesClient
        Places.initialize(requireActivity(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireActivity())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.map?.setOnMapLongClickListener(this@CreateRouteFragment)

        initListeners()
        initCollectors()
    }

    private fun initCollectors() {
        lifecycleScope.launch {
            sharedViewModel.searchForLocationResponse.collect {
                sharedViewModel.fetchPlaceResponse.collect { fetchPlaceResponse ->
                    binding.locationListTextView.text = fetchPlaceResponse?.place?.types?.toString()
                    fetchPlaceResponse?.place?.latLng?.let {  place ->
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, 15f)
                        map?.moveCamera(cameraUpdate)
                        map?.addMarker(MarkerOptions().position(place))
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                sharedViewModel.searchForLocation(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Optionally, you can provide search suggestions here.
                    return false
            }
        })

        binding.clearMarkersButton.setOnClickListener {
            map?.clear()
        }

        binding.createRouteButton.setOnClickListener {
            // viewModel.getRoute()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        this.map?.setOnMapLongClickListener(this)
    }

    override fun onMapLongClick(pos: com.google.android.gms.maps.model.LatLng) {
        map?.addMarker(MarkerOptions().position(pos))
        viewModel.addToLocationList(
            LatLng(
                longitude = pos.longitude,
                latitude = pos.latitude
            )
        )

        binding.locationListTextView.text = viewModel.setLocationListText(
            binding.locationListTextView.text.toString(),
            "${pos.longitude.toFloat()}-${pos.latitude.toFloat()}"
        )
    }
}