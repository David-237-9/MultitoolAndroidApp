package dev.david2379.multitoolandroidapp.ui.gps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.david2379.multitoolandroidapp.logic.gps.model.GPSLocation
import java.text.DateFormat
import java.util.Date

@Composable
fun GpsScreen(gpsData: GPSLocation?) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (gpsData != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "GPS Information",
                            style = MaterialTheme.typography.titleLarge
                        )

                        HorizontalDivider()

                        InfoRow(
                            label = "Coordinates",
                            value = "${gpsData.latitude}, ${gpsData.longitude}"
                        )

                        InfoRow(
                            label = "Altitude Above Sea Level",
                            value = "%.2f".format(gpsData.altitude) + " meters"
                        )

                        InfoRow(
                            label = "Vertical Accuracy",
                            value = "%.2f".format(gpsData.verticalAccuracyMeters) + " meters"
                        )

                        InfoRow(
                            label = "Last Update",
                            value = DateFormat.getDateTimeInstance().format(Date(gpsData.timestamp))
                        )

                        InfoRow(
                            label = "Calculated Speed",
                            value = "%.2f".format(gpsData.lastCalculatedSpeed) + " km/h",
                        )

                        InfoRow(
                            label = "Average Speed",
                            value = "%.2f".format(gpsData.averageSpeed) + " km/h",
                        )

                        InfoRow(
                            label = "Satellites in Use",
                            value = gpsData.satelliteCount.toString()
                        )
                    }
                }
            } else {
                EmptyState()
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No GPS Data Available",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Waiting for location updates…",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
