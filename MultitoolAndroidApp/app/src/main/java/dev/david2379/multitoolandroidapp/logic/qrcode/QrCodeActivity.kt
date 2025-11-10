package dev.david2379.multitoolandroidapp.logic.qrcode

import android.graphics.Bitmap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dev.david2379.multitoolandroidapp.logic.qrcode.model.QrCodeGenerator
import dev.david2379.multitoolandroidapp.ui.qrcode.QrCodeScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val QR_GENERATION_DEBOUNCE = 1000L

class QrCodeActivity: ComponentActivity() {
    private var qrGeneratorJob: Job? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }

            fun startQrGeneration(text: String) {
                qrGeneratorJob?.cancel()
                qrGeneratorJob = lifecycleScope.launch(Dispatchers.Default) {
                    delay(QR_GENERATION_DEBOUNCE) // Debounce
                    qrCodeBitmap = if (text.isEmpty()) null
                    else QrCodeGenerator.generate(text)
                }
            }

            MultitoolAndroidAppTheme {
                QrCodeScreen(
                    "QR Code Tool",
                    onTextChange = { startQrGeneration(it) },
                    qrCodeBitmap = qrCodeBitmap,
                )
            }
        }
    }
}
