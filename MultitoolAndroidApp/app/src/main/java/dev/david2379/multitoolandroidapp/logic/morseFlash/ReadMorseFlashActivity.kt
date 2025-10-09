package dev.david2379.multitoolandroidapp.logic.morseFlash

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraMetadata
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2Interop
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import dev.david2379.multitoolandroidapp.logic.morseFlash.model.MorseFlashDecoder
import dev.david2379.multitoolandroidapp.ui.morseFlash.ReadMorseFlashScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ReadMorseFlashActivity : ComponentActivity() {
    private lateinit var cameraExecutor: ExecutorService

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted) startCamera() }

    private var onBrightnessUpdate: ((Double) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            var brightness by remember { mutableDoubleStateOf(0.0) }
            var decoder by remember { mutableStateOf(MorseFlashDecoder()) }

            // Save lambda so CameraX thread can update brightness
            onBrightnessUpdate = { newVal ->
                brightness = newVal
                decoder.onBrightnessUpdate(newVal)
            }

            MultitoolAndroidAppTheme {
                ReadMorseFlashScreen("Read Morse Flash", brightness, decoder.text)
            }
        }

        // Request permission or start camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) startCamera()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val camera2Extender = Camera2Interop.Extender(ImageAnalysis.Builder())

            camera2Extender.setCaptureRequestOption(
                CaptureRequest.CONTROL_AE_MODE,
                CameraMetadata.CONTROL_AE_MODE_OFF
            )
            camera2Extender.setCaptureRequestOption(
                CaptureRequest.CONTROL_AWB_MODE,
                CameraMetadata.CONTROL_AWB_MODE_OFF
            )

            camera2Extender.setCaptureRequestOption(
                CaptureRequest.SENSOR_EXPOSURE_TIME,
                10_000_000L   // 10 milliseconds
            )
            camera2Extender.setCaptureRequestOption(
                CaptureRequest.SENSOR_SENSITIVITY,
                800           // ISO 800
            )

            val analyzerBuilder = ImageAnalysis.Builder()
            Camera2Interop.Extender(analyzerBuilder).apply {
                setCaptureRequestOption(
                    CaptureRequest.CONTROL_AE_MODE,
                    CameraMetadata.CONTROL_AE_MODE_OFF
                )
                setCaptureRequestOption(
                    CaptureRequest.CONTROL_AWB_MODE,
                    CameraMetadata.CONTROL_AWB_MODE_OFF
                )
                setCaptureRequestOption(
                    CaptureRequest.SENSOR_EXPOSURE_TIME,
                    10_000_000L // 10 ms
                )
                setCaptureRequestOption(
                    CaptureRequest.SENSOR_SENSITIVITY,
                    800
                )
            }

            val analyzer = analyzerBuilder
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        lifecycleScope.launch(Dispatchers.Default) {
                            onBrightnessUpdate?.invoke(luma)
                        }
                    })
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, CameraSelector.DEFAULT_BACK_CAMERA, analyzer
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }


    private class LuminosityAnalyzer(private val listener: (Double) -> Unit) : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            val buffer: ByteBuffer = image.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            val luma = data.map { it.toInt() and 0xFF }.average()
            listener(luma)
            image.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
