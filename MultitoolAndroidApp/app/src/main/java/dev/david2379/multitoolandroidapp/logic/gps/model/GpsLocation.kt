package dev.david2379.multitoolandroidapp.logic.gps.model

data class GPSLocation (
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val verticalAccuracyMeters: Float,
    val lastCalculatedSpeed: Float,
    val calculatedSpeedList: List<Float>,
    val averageSpeed: Float,
    val satelliteCount: Int,
)
