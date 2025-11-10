package dev.david2379.multitoolandroidapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.david2379.multitoolandroidapp.logic.camera.CameraActivity
import dev.david2379.multitoolandroidapp.logic.gps.GpsActivity
import dev.david2379.multitoolandroidapp.logic.morseFlash.MorseFlashActivity
import dev.david2379.multitoolandroidapp.logic.qrcode.QrCodeActivity
import dev.david2379.multitoolandroidapp.ui.MainScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val navigateToCameraIntent: Intent by lazy {
            Intent(this, CameraActivity::class.java)
        }

        val navigateToMorseFlashIntent: Intent by lazy {
            Intent(this, MorseFlashActivity::class.java)
        }

        val navigateToGpsIntent: Intent by lazy {
            Intent(this, GpsActivity::class.java)
        }

        val navigateToQrCodeIntent: Intent by lazy {
            Intent(this, QrCodeActivity::class.java)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultitoolAndroidAppTheme {
                MainScreen(
                    navigates = listOf(
                        Pair("Camera") { startActivity(navigateToCameraIntent) },
                        Pair("Morse Flash") { startActivity(navigateToMorseFlashIntent) },
                        Pair("GPS") { startActivity(navigateToGpsIntent) },
                        Pair("QR Code") { startActivity(navigateToQrCodeIntent) },
                    ),
                )
            }
        }
    }
}
