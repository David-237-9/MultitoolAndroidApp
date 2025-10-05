package dev.david2379.multitoolandroidapp.logic.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import dev.david2379.multitoolandroidapp.ui.camera.CameraScreen

class CameraActivity : ComponentActivity() {
    private var previewView: PreviewView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkCameraPermission()

        setContent {
            CameraScreen(onPreviewReady = {
                previewView = it
                if (camPermission) startCamera() // Start camera only if permission is granted
            })
        }
    }

    val camPermission get() = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    /**
     * Handles the asynchronous result of the permission request.
     * If granted and preview is ready, starts the camera; if denied, shows a toast message.
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) previewView?.let { startCamera() } // Only start camera if previewView is ready
            else Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }


    /**
     * Check for camera permission and request if not granted
     */
    fun checkCameraPermission() {
        if (!camPermission)
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        // Do not start camera here, wait for previewView to be ready
    }

    /**
     * Sets up CameraX and starts the camera preview
     * This function is supposed to be called only when camera permission is granted
     * and previewView is ready to avoid errors.
     */
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
            val viewFinder = previewView ?: return@addListener

            preview.surfaceProvider = viewFinder.surfaceProvider
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview)
            } catch (exc: Exception) {
                Log.e("CameraX", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }
}
