package dev.david2379.multitoolandroidapp.ui.gps

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.david2379.multitoolandroidapp.logic.gps.model.GPSLocation
import java.util.Date

@Composable
fun GpsScreen(gpsData: GPSLocation?) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (gpsData != null) {
                Text("Latitude: ${gpsData.latitude}")
                Text("Longitude: ${gpsData.longitude}")
                Text("Timestamp: ${Date(gpsData.timestamp)}")
                Text("Location Speed: ${gpsData.locationSpeed}kmh")
                Text("Location Calculated Speed: ${gpsData.locationCalculatedSpeed}kmh")
                Text("Location Manually Calculated Speed: ${gpsData.locationManuallyCalculatedSpeed}kmh")
            } else {
                Text("No GPS Data")
            }
        }
    }
}
