package com.wheretogo.placesandroutesrecommenderapp.ui.maps.createroute

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.ecemsevvalcinar.easyroute.EasyRoutesDirections
import com.ecemsevvalcinar.easyroute.EasyRoutesDrawer
import com.ecemsevvalcinar.easyroute.drawRoute
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCreateRouteBinding
import com.wheretogo.placesandroutesrecommenderapp.extension.ifFalse
import com.wheretogo.placesandroutesrecommenderapp.extension.ifTrue
import com.wheretogo.placesandroutesrecommenderapp.model.Location
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.MapsSharedViewModel
import com.wheretogo.placesandroutesrecommenderapp.util.MapUtility.DEFAULT_ZOOM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateRouteFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private var _binding: FragmentCreateRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreateRouteViewModel by viewModels()
    private val sharedViewModel: MapsSharedViewModel by viewModels()
    private var placeIndex = 1

    private var map: GoogleMap? = null
    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val markersList: ArrayList<Marker> = arrayListOf()
    // temp list from recommendation fragment
    private var locationList: ArrayList<Location> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRouteBinding.inflate(inflater, container, false)

        // if from recommendation fragment
        arguments?.let {
            locationList = it.getParcelableArrayList<Location>("placeList") as ArrayList<Location>
        }
        // if from recommendation fragment
        locationList.isNotEmpty().ifTrue {
            viewModel.setLocationList(locationList.toList())
        }
        binding.apply {
            viewModel = viewModel
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync {
                map = it
                map?.setOnMapLongClickListener(this@CreateRouteFragment)
                ifLocationListFromRecommendationFragmentIsNotEmpty(it)
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
                    fetchPlaceResponse?.place?.let {  place ->
                        viewModel.addToLocationList(place)
                        place.latLng?.let {
                            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(it, DEFAULT_ZOOM)
                            map?.moveCamera(cameraUpdate)
                            map?.addMarker(MarkerOptions().position(it).title(placeIndex.toString()).snippet(place.name))?.showInfoWindow()
                            placeIndex += 1
                        }
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
            viewModel.clearLocationList()
            binding.searchView.setQuery("", true)
            binding.searchView.clearFocus()
            placeIndex = 1
        }

        binding.createRouteButton.setOnClickListener {
            viewModel.locationList.value.isNullOrEmpty().not().ifTrue {
                val placeDirections = viewModel.createEasyRouteDirectionsFromUserSearch()
                drawRouteForPoints(placeDirections)
                // viewModel.setLocationListPositions()
            }.ifFalse {
                Toast.makeText(requireActivity(), "You should chose locations to create a route!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun ifLocationListFromRecommendationFragmentIsNotEmpty(map: GoogleMap) {
        viewModel.recommendedLocationList.isEmpty().not().ifTrue {
            for (i in viewModel.recommendedLocationList) {
                var latitude = .0
                var longitude = .0
                i.latitude?.let {
                    latitude = it.toDouble()
                }
                i.longitude?.let {
                    longitude = it.toDouble()
                }
                map.addMarker(
                    MarkerOptions().position(LatLng(latitude, longitude))
                        .title((viewModel.recommendedLocationList.indexOf(i) + 1).toString())
                        .snippet(i.locationName)
                )
            }
            val placeDirections = viewModel.createEasyRouteDirectionsFromRecommendationList()
            drawRouteForPoints(placeDirections)
            var latitude = .0
            var longitude = .0
            viewModel.recommendedLocationList[0].latitude?.let {
                latitude = it.toDouble()
            }
            viewModel.recommendedLocationList[0].longitude?.let {
                longitude = it.toDouble()
            }
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), DEFAULT_ZOOM)
            map.moveCamera(cameraUpdate)
        }
    }

    private fun drawRouteForPoints(placeDirections: EasyRoutesDirections) {
        val routeDrawer = EasyRoutesDrawer.Builder(map!!)
            .pathWidth(10f)
            .pathColor(Color.GREEN)
            .geodesic(true)
            .previewMode(false)
            .build()

        val customPolylineOptions = PolylineOptions()
        customPolylineOptions.color(
            ContextCompat.getColor(
                requireContext(),
                R.color.main_color_orange
            )
        )
        customPolylineOptions.width(15f)

        val routeDrawerWithCustomPolyline =
            EasyRoutesDrawer.Builder(map!!, customPolylineOptions)
                .previewMode(false)
                .build()

        map?.drawRoute(
            easyRoutesDirections = placeDirections,
            routeDrawer = routeDrawer,
            markersListCallback = { markers -> markersList.addAll(markers) },
            googleMapsLink = { url -> Log.d("GoogleLink", url) }
        ) { legs ->
            legs?.forEach {
                Log.d("Point startAddress:", it?.startAddress.toString())
                Log.d("Point endAddress:", it?.endAddress.toString())
                Log.d("Distance:", it?.distance.toString())
                Log.d("Duration:", it?.duration.toString())
            }
        }
    }


    override fun onMapLongClick(pos: LatLng) {
        map?.addMarker(MarkerOptions().position(pos))
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
}