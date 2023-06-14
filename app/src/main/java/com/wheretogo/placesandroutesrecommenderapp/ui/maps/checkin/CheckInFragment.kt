package com.wheretogo.placesandroutesrecommenderapp.ui.maps.checkin

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.wheretogo.placesandroutesrecommenderapp.R
import com.wheretogo.placesandroutesrecommenderapp.databinding.FragmentCheckInBinding
import com.wheretogo.placesandroutesrecommenderapp.extension.ifTrue
import com.wheretogo.placesandroutesrecommenderapp.extension.isNull
import com.wheretogo.placesandroutesrecommenderapp.ui.maps.MapsSharedViewModel
import com.wheretogo.placesandroutesrecommenderapp.util.MapUtility
import com.wheretogo.placesandroutesrecommenderapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentCheckInBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: MapsSharedViewModel by viewModels()
    private lateinit var adapter: CategoryItemAdapter
    private val viewModel: CheckInViewModel by viewModels()
    private val args by navArgs<CheckInFragmentArgs>()

    private var recommendedLocation: com.wheretogo.placesandroutesrecommenderapp.model.Location? = null

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(38.375701, 27.191901)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCheckInBinding.inflate(inflater, container, false)

        arguments?.let {
            recommendedLocation = it.getParcelable("recommendedLocation")
        }
        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync {
            map = it

            recommendedLocation?.let { loc ->
                var latitude = .0
                var longitude = .0
                loc.latitude?.let { lati ->
                    latitude = lati.toDouble()
                }
                loc.longitude?.let { long ->
                    longitude = long.toDouble()
                }

                map?.addMarker(MarkerOptions().position(LatLng(latitude, longitude))
                    .title(loc.locationName))
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f)
                map?.moveCamera(cameraUpdate)
                binding.selectedPlace.text = loc.locationName
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner
        adapter = CategoryItemAdapter(mutableListOf(), ::onItemSelect)
        setUpRecyclerView()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(MapUtility.KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(MapUtility.KEY_CAMERA_POSITION)
        }

        // Construct a PlacesClient
        Places.initialize(requireActivity(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireActivity())

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

    }
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(MapUtility.KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(MapUtility.KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocationPermission()

        initListeners()
        initCollectors()
    }

    private fun onItemSelect(category: CategoryModel) {
        viewModel.setSelectedCategory(category)
        binding.executePendingBindings()
    }

    private fun initListeners() {
        binding.findLocationIcon.setOnClickListener {
            map?.clear()
            showCurrentPlace()
        }

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

        binding.makeCheckInButton.setOnClickListener {
            args.userId?.let { id ->
                binding.categoryEditText.text.isNullOrEmpty().not().ifTrue {
                    viewModel.setSelectedCategoryString(binding.categoryEditText.text.toString())
                }
                viewModel.addCheckInToFirebase(id)
            }
        }
    }

    private fun initCollectors() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.searchForLocationResponse.collect {
                sharedViewModel.fetchPlaceResponse.collect { fetchPlaceResponse ->
                    binding.apply {
                        if (fetchPlaceResponse?.place?.name.isNullOrEmpty().not()) {
                            selectedPlace.text = fetchPlaceResponse?.place?.name
                            viewModel.setSelectedName(fetchPlaceResponse?.place?.name)
                        }
                        if (fetchPlaceResponse?.place?.latLng?.latitude.isNull() ||fetchPlaceResponse?.place?.latLng?.longitude.isNull()) {
                            placeCoordination.text = fetchPlaceResponse?.place?.latLng.toString()
                            viewModel.setSelectedLatlng(fetchPlaceResponse?.place?.latLng)
                        }
                        val data: MutableList<CategoryModel?> = mutableListOf()
                        for (i in sharedViewModel.placeCategoryPredictionList) {
                            data.add(CategoryModel(category = i))
                            if (data.size > 3) {
                                break
                            }
                        }
                        binding.categoryEditText.text.clear()
                        binding.categoryEditText.visibility = View.GONE
                        binding.categoryRecyclerView.visibility = View.VISIBLE
                        data.toList().let { list -> adapter.setData(list) }
                        adapter.notifyDataSetChanged()
                        binding.executePendingBindings()
                    }
                    fetchPlaceResponse?.place?.latLng?.let {  place ->
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(place, 15f)
                        map?.moveCamera(cameraUpdate)
                        binding.makeCheckInButton.isEnabled = true
                        map?.clear()
                        map?.addMarker(MarkerOptions().position(place))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.addCheckInFlow.collect {
                when (it) {
                    is Resource.Failure -> {
                        binding.progressBarLoading.visibility = View.GONE
                        Toast.makeText(requireActivity(), it.exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Success -> {
                        Toast.makeText(requireActivity(), "Check in added!.", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.feedFragment)
                    }
                    is Resource.Loading -> { binding.progressBarLoading.visibility = View.VISIBLE }
                    else -> {}
                }
            }
        }
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity().applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapUtility.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            MapUtility.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }

    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        this.map = map

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }

            override fun getInfoContents(marker: Marker): View {
                // Inflate the layouts for the info window, title and snippet.
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    binding.mapView, false)
                val title = infoWindow.findViewById<TextView>(R.id.customInfoTitle)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView>(R.id.customInfoSnippet)
                snippet.text = marker.snippet
                return infoWindow
            }
        })

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

    }

    private fun getDeviceLocation() {
        /*
    * Get the best and most recent location of the device, which may be null in rare
    * cases when a location is not available.
    */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), MapUtility.DEFAULT_ZOOM))
                        }
                    } else {
                        Log.d("TAG", "Current location is null. Using defaults.")
                        Log.e("TAG", "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, MapUtility.DEFAULT_ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            binding.progressBarLoading.visibility = View.VISIBLE
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < MapUtility.M_MAX_ENTRIES) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        MapUtility.M_MAX_ENTRIES
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e("TAG", "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i("TAG", "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            binding.progressBarLoading.visibility = View.GONE
            binding.makeCheckInButton.isEnabled = true
            map?.addMarker(MarkerOptions()
                .title(getString(R.string.default_info_title))
                .position(defaultLocation)
                .snippet(getString(R.string.default_info_snippet)))

            // Prompt the user for permission.
            getLocationPermission()
        }
    }

    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { _, which -> // The "which" argument contains the position of the selected item.
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                $markerSnippet
                ${likelyPlaceAttributions[which]}
                """.trimIndent()
            }

            if (markerLatLng == null) {
                return@OnClickListener
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map?.addMarker(
                MarkerOptions()
                .title(likelyPlaceNames[which])
                .position(markerLatLng)
                .snippet(markerSnippet))

            // Position the map's camera at the location of the marker.
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                MapUtility.DEFAULT_ZOOM))
            binding.selectedPlace.text = likelyPlaceNames[which]
            viewModel.setSelectedName(likelyPlaceNames[which])
            binding.placeCoordination.text =
                "$markerLatLng"
            viewModel.setSelectedLatlng(markerLatLng)
            binding.progressBarLoading.visibility = View.GONE
            binding.categoryEditText.visibility = View.VISIBLE
            binding.categoryRecyclerView.visibility = View.GONE
            binding.makeCheckInButton.isEnabled = true
        }

        // Display the dialog.
        AlertDialog.Builder(requireActivity())
            .setTitle(R.string.pick_place)
            .setItems(likelyPlaceNames, listener)
            .show()
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

    private fun setUpRecyclerView() {
        val layoutManager = FlexboxLayoutManager(requireActivity())
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.CENTER
        binding.categoryRecyclerView.layoutManager = layoutManager
        binding.categoryRecyclerView.adapter = adapter
    }
}