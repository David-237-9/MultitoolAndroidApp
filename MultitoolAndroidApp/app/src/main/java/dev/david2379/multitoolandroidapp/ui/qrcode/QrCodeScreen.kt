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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import dev.david2379.multitoolandroidapp.ui.general.TopBar

@Composable
fun QrCodeScreen(
    title: String,
    qrCodeBitmap: Bitmap?,
    inputText: String,
    onTextChange: (String) -> Unit,
    correctionLevel: Int,
    onCorrectionErrorLevelChange: () -> Unit,
    qrSize: Int,
    onQrSizeChange: () -> Unit,
) {
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
            DataInput(
                inputText,
                onTextChange,
                correctionLevel,
                onCorrectionErrorLevelChange,
                qrSize,
                onQrSizeChange,
            )

            Spacer(modifier = Modifier.height(16.dp))

            qrCodeBitmap?.let { QrCodeDisplay(it) } // Only display qr if not null
        }
    }
}

@Composable
private fun DataInput(
    inputText: String,
    onTextChange: (String) -> Unit,
    correctionLevel: Int,
    onCorrectionErrorLevelChange: () -> Unit,
    qrSize: Int,
    onQrSizeChange: () -> Unit,
) {
    TextField(
        value = inputText,
        onValueChange = onTextChange,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
    Button(onClick = { onCorrectionErrorLevelChange() }) {
        Text("Error Correction: $correctionLevel%")
    }
    /*
    Button(onClick = { onQrSizeChange() }) {
        Text("Size: ${qrSize}x${qrSize}")
    }
    */
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
