package dev.david2379.multitoolandroidapp.logic.gps

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dev.david2379.multitoolandroidapp.logic.gps.model.GPSLocation
import dev.david2379.multitoolandroidapp.logic.gps.model.GPSLocationGetter
import dev.david2379.multitoolandroidapp.logic.gps.model.LocationPermission
import dev.david2379.multitoolandroidapp.ui.gps.GpsScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val GPS_REFRESH_RATE_MS = 500L

class GpsActivity : ComponentActivity() {
    private var refreshGpsJob: Job? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        LocationPermission.init(this) // Request location permission if not granted

        val gpsData = GPSLocationGetter(this)

        setContent {
            var gpsLocation by remember { mutableStateOf<GPSLocation?>(null) }

            fun startRefreshJob () {
                refreshGpsJob?.cancel()
                refreshGpsJob = lifecycleScope.launch { // Start a coroutine to refresh GPS data
                    val startTime = System.currentTimeMillis()
                    gpsData.getLocation(
                        onResult = { newLocation ->
                            gpsLocation = newLocation
                            val waitTime = GPS_REFRESH_RATE_MS - (System.currentTimeMillis() - startTime)
                            lifecycleScope.launch {
                                if (waitTime > 0) delay(waitTime)
                                startRefreshJob()
                            }
                        },
                        gpsLocation,
                    )
                }
            }
            startRefreshJob()

            MultitoolAndroidAppTheme {
                GpsScreen(gpsLocation)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        refreshGpsJob?.cancel()
    }
}
