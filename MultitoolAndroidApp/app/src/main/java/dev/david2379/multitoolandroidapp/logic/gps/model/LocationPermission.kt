package dev.david2379.multitoolandroidapp.logic.gps.model

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val LOCATION_PERMISSION_REQUEST_CODE = 100

object LocationPermission {
    /**
     * Initialize location permission by checking and requesting if not granted
     */
    fun init(activity: Activity) {
        if (hasFineLocationPermission(activity)) {
            requestLocationPermission(activity)
        }
    }

    /**
     * Check if the app has location permission
     */
    private fun hasFineLocationPermission(activity: Activity): Boolean =
        (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)

    /**
     * Request location permission
     */
    private fun requestLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}
