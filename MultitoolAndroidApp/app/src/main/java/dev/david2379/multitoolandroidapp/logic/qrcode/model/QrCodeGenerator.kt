// kotlin
package dev.david2379.multitoolandroidapp.logic.qrcode.model

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object QrCodeGenerator {
    /**
     * Gera um Bitmap QR code a partir do texto.
     * @param text conteúdo do QR
     * @param size largura/altura em pixels (padrão 512)
     * @param margin margem do QR (padrão 1)
     * @throws IllegalArgumentException se o texto for vazio
     */
    fun generate(text: String, size: Int = 512, margin: Int = 1): Bitmap {
        require(text.isNotEmpty()) { "text must not be empty" }

        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8",
            EncodeHintType.MARGIN to margin
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
