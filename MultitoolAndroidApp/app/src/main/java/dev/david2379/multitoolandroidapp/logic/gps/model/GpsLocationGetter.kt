package dev.david2379.multitoolandroidapp.logic.gps.model

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.*
import kotlin.compareTo
import kotlin.div
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

const val SPEED_LIST_SIZE = 5

class GPSLocationGetter(private val activity: Activity) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    private var lastLocation: GPSLocation? = null
    private var onUpdate: ((GPSLocation?) -> Unit)? = null

    private val satelliteCounter = GPSSatelliteCounter(activity)

    private val locationCallback = object : LocationCallback() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLocationResult(result: LocationResult) {
            val successLocation = result.lastLocation ?: return
            val currentTime = System.currentTimeMillis()

            val calculatedSpeed = lastLocation?.let { meterSecondToKilometerHour(
                calculateSpeed(
                    manuallyCalcLocationsDistance(
                        successLocation.latitude,
                        successLocation.longitude,
                        it.latitude,
                        it.longitude,
                    ),
                    currentTime - it.timestamp
                )
            ) } ?: 0f

            val calculatedSpeedList = lastLocation?.calculatedSpeedList?.let {
                if (it.size >= SPEED_LIST_SIZE) it.drop(1) + calculatedSpeed
                else it + calculatedSpeed
            } ?: listOf(calculatedSpeed)

            val averageSpeed = if (calculatedSpeedList.isNotEmpty()) {
                calculatedSpeedList.sum() / calculatedSpeedList.size
            } else 0f

            val newLocation = GPSLocation(currentTime,
                successLocation.latitude,
                successLocation.longitude,
                successLocation.altitude,
                successLocation.verticalAccuracyMeters,
                calculatedSpeed,
                calculatedSpeedList,
                averageSpeed,
                satelliteCounter.getSatelliteCount()
            )

            lastLocation = newLocation
            onUpdate?.invoke(newLocation)
        }
    }

    /**
     * Start continuous GPS updates.
     * The callback returns fine-grained GPS location with speed calculations.
     */
    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates(onResult: (GPSLocation?) -> Unit) {
        this.onUpdate = onResult

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // update interval in ms
        )
            .setWaitForAccurateLocation(true) // ensures GPS fix
            .setMinUpdateIntervalMillis(500L)
            .setMaxUpdateDelayMillis(0L)
            .build()

        fusedLocationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        satelliteCounter.startSatelliteListener()
    }

    /**
     * Stop GPS updates to save battery.
     */
    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        onUpdate = null
    }

    /**
     * Calculate speed in meters/second
     */
    private fun calculateSpeed(distanceMeters: Float, timeMillis: Long): Float {
        if (timeMillis == 0L) return 0f
        return (distanceMeters / (timeMillis / 1000f))
    }

    private fun meterSecondToKilometerHour(speed: Float): Float = speed * 3.6f

    private fun manuallyCalcLocationsDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Float {
        val earthRadius = 6371000.0 // meters
        val dLat = Math.toRadians(latitude2 - latitude1)
        val dLon = Math.toRadians(longitude2 - longitude1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(latitude1)) * cos(Math.toRadians(latitude2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return (earthRadius * c).toFloat()
    }
}
