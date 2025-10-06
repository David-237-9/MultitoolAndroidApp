package dev.david2379.multitoolandroidapp.logic.gps.model

data class GPSLocation (
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val locationSpeed: Float,
    val locationCalculatedSpeed: Float,
    val locationManuallyCalculatedSpeed: Float,
)
