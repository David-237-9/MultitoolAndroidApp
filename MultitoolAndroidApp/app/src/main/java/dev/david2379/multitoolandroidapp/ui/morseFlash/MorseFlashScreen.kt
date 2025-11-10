package dev.david2379.multitoolandroidapp.ui.morseFlash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.david2379.multitoolandroidapp.ui.general.TopBar

@Composable
fun MorseFlashScreen(
    title: String,
    onReadMorseFlashNavigate: () -> Unit,
    setState: (String, Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar(title) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onReadMorseFlashNavigate,
            ) {
                Text("Read Morse Flash")
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Type the text") }
            )
            Button(
                onClick = { setState(text, true) },
            ) {
                Text("Start")
            }
            Button(
                onClick = { setState(text, false) },
            ) {
                Text("Stop")
            }
        }
    }
}
