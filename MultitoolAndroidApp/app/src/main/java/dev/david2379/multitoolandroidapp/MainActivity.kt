package dev.david2379.multitoolandroidapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.david2379.multitoolandroidapp.logic.camera.CameraActivity
import dev.david2379.multitoolandroidapp.ui.MainScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val navigateToCameraIntent: Intent by lazy {
            Intent(this, CameraActivity::class.java)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultitoolAndroidAppTheme {
                MainScreen(
                    navigates = listOf(
                        Pair("Camera") { startActivity(navigateToCameraIntent) },
                    ),
                )
            }
        }
    }
}
