package dev.david2379.multitoolandroidapp.logic.gps.model

import android.Manifest
import android.app.Activity
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class GPSLocationGetter(activity: Activity) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getLocation(onResult: (GPSLocation?) -> Unit, lastLocation: GPSLocation?) {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { successLocation ->
            if (successLocation != null) {
                val currentTime = System.currentTimeMillis()
                onResult(
                    GPSLocation(
                        currentTime,
                        successLocation.latitude,
                        successLocation.longitude,
                        successLocation.speed,
                        if (lastLocation != null) calculateSpeed(
                            calcLocationsDistance(
                                successLocation.latitude,
                                successLocation.longitude,
                                lastLocation.latitude,
                                lastLocation.longitude,
                            ),
                            currentTime - lastLocation.timestamp
                        ) else 0f,
                        if (lastLocation != null) meterSecondToKilometerHour(
                            calculateSpeed(
                                manuallyCalcLocationsDistance(
                                    successLocation.latitude,
                                    successLocation.longitude,
                                    lastLocation.latitude,
                                    lastLocation.longitude,
                                ),
                                currentTime - lastLocation.timestamp
                            )
                        ) else 0f,
                    )
                )
            } else {
                onResult(null)
            }
        }.addOnFailureListener { exception ->
            onResult(null)
        }
    }

    /**
     * Calculate speed in meters/second
     */
    private fun calculateSpeed(distanceMeters: Float, timeMillis: Long): Float {
        if (timeMillis == 0L) return 0f
        return (distanceMeters / (timeMillis / 1000f))
    }

    private fun meterSecondToKilometerHour(speed: Float): Float = speed * 3.6f

    private fun calcLocationsDistance(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Float {
        val results = FloatArray(1)
        android.location.Location.distanceBetween(
            latitude1, longitude1,
            latitude2, longitude2,
            results
        )
        return results[0]
    }

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
