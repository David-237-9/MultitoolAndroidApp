package dev.david2379.multitoolandroidapp.ui.morseFlash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ReadMorseFlashScreen(
    title: String,
    brightness: Double,
    text: String,
) {
    Scaffold(
        topBar = { TopBar(title) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text("Brightness: $brightness")
            Text("Text: $text")
        }
    }
}

@Composable
private fun TopBar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .padding(8.dp)
        ) {
            Text(
                text = title,
            )
        }
    }
}
