package dev.david2379.multitoolandroidapp.ui.qrcode

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import dev.david2379.multitoolandroidapp.ui.general.TopBar

@Composable
fun QrCodeScreen(
    title: String,
    onTextChange: (String) -> Unit,
    qrCodeBitmap: Bitmap?,
) {
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopBar(title) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextInput(inputText = inputText) { text ->
                inputText = text
                onTextChange(text)
            }

            Spacer(modifier = Modifier.height(16.dp))

            qrCodeBitmap?.let { QrCodeDisplay(it) } // Only display qr if not null
        }
    }
}

@Composable
private fun TextInput(
    inputText: String,
    onTextChange: (String) -> Unit,
) {
    TextField(
        value = inputText,
        onValueChange = onTextChange,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun QrCodeDisplay(
    qrCodeBitmap: Bitmap,
) {
    Image(
        bitmap = qrCodeBitmap.asImageBitmap(),
        contentDescription = "QR Code",
        modifier = Modifier.size(256.dp)
    )
}
