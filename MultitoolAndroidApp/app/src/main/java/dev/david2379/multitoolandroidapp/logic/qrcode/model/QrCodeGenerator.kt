// kotlin
package dev.david2379.multitoolandroidapp.logic.qrcode.model

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QrCodeGenerator {
    val supportedErrorCorrections = listOf(7, 15, 25, 30)

    /**
     * Generates a QR code bitmap from the given text.
     * @param text The text to encode in the QR code.
     * @param size The width and height of the generated QR code bitmap in pixels.
     * @param margin The margin (quiet zone) around the QR code.
     * @param errorCorrection The error correction level (7%, 15%, 25%, 30%).
     * @return A Bitmap representing the generated QR code.
     * @throws IllegalArgumentException if the text is empty or encoding fails.
     */
    fun generate(
        text: String,
        size: Int = 512,
        margin: Int = 1,
        errorCorrection: Int = 15
    ): Bitmap {
        require(text.isNotEmpty()) { "text must not be empty" }

        val errorCorrectionLevel = when (errorCorrection) {
            7 -> ErrorCorrectionLevel.L // 7% error recovery
            15 -> ErrorCorrectionLevel.M // 15% error recovery
            25 -> ErrorCorrectionLevel.Q // 25% error recovery
            30 -> ErrorCorrectionLevel.H // 30% error recovery
            else -> ErrorCorrectionLevel.M // Default (15% error recovery)
        }

        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.MARGIN to margin,
            EncodeHintType.ERROR_CORRECTION to errorCorrectionLevel
        )

        val bitMatrix: BitMatrix = try {
            MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, size, size, hints)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to encode QR code: ${e.message}", e)
        }

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMatrix.get(x, y)) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
            }
        }

        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }
}
