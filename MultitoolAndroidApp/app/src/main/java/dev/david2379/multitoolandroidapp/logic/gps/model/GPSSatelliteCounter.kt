package dev.david2379.multitoolandroidapp.logic.gps.model

import android.Manifest
import android.app.Activity
import android.location.GnssStatus
import android.location.LocationManager
import androidx.annotation.RequiresPermission

class GPSSatelliteCounter(activity: Activity) {

    private val locationManager = activity.getSystemService(LocationManager::class.java)
    private var satelliteCount = 0

    /**
     * Starts listening for GNSS satellite status updates.
     * Requires ACCESS_FINE_LOCATION permission.
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun startSatelliteListener() {
        locationManager.registerGnssStatusCallback(object : GnssStatus.Callback() {
            override fun onSatelliteStatusChanged(status: GnssStatus) {
                super.onSatelliteStatusChanged(status)

                satelliteCount = (0 until status.satelliteCount).count { status.usedInFix(it) }
            }
        })
    }

    /**
     * Returns the current count of satellites used in the GPS.
     * @return Number of satellites
     */
    fun getSatelliteCount(): Int = satelliteCount
}
