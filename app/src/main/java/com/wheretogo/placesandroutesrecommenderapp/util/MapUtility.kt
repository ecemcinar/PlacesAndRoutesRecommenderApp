package com.wheretogo.placesandroutesrecommenderapp.util

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import pub.devrel.easypermissions.EasyPermissions

object MapUtility {

    const val KEY_CAMERA_POSITION = "camera_position"
    const val KEY_LOCATION = "location"
    const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 100
    const val DEFAULT_ZOOM = 15f
    const val M_MAX_ENTRIES = 1

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    fun hasLocationPermissions(context: Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
}