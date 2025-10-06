package dev.david2379.multitoolandroidapp.logic.morseFlash.model

import android.content.Context
import android.hardware.camera2.CameraManager

class FlashHelper(context: Context) {
    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val cameraId = cameraManager.cameraIdList.firstOrNull { id ->
        cameraManager.getCameraCharacteristics(id)
            .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
    }

    fun turnOnFlash() {
        cameraId?.let {
            cameraManager.setTorchMode(it, true)
        }
    }

    fun turnOffFlash() {
        cameraId?.let {
            cameraManager.setTorchMode(it, false)
        }
    }
}
