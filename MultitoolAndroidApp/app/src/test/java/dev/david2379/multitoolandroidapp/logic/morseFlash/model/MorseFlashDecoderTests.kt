package dev.david2379.multitoolandroidapp.logic.morseFlash.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

class MorseFlashDecoderTests {
    @Test
    fun perfectConditionsTest() {
        val decoder = MorseFlashDecoder()
        val scope = CoroutineScope(Dispatchers.Default)
        val text = "SOS SOS"
        val divide = 4
        val morse: List<Morse.Signal> = prepareMorse(text)

        // Send signals
        for (signal in morse) {
            val brightness = if (signal == Morse.Signal.Dot || signal == Morse.Signal.Dash) 255.0 else 0.0
            repeat((signal.time * divide) / TIME_UNIT) {
                scope.launch { decoder.onBrightnessUpdate(brightness) }
                Thread.sleep(TIME_UNIT / divide.toLong())
            }
        }

        // Assert
        val expected = " $text"
        assert(decoder.text == expected) { "Expected '$expected' but got '${decoder.text}'" }
    }

    @Test
    fun noisyConditionsTest() {
        val decoder = MorseFlashDecoder()
        val scope = CoroutineScope(Dispatchers.Default)
        val text = "SOS SOS"
        val divide = 2
        val morse: List<Morse.Signal> = prepareMorse(text)

        // Send signals
        for (signal in morse) {
            repeat((signal.time * divide) / TIME_UNIT) {
                scope.launch {
                    val brightness = if (signal == Morse.Signal.Dot || signal == Morse.Signal.Dash)
                        Math.random() * 200 + 55 // between 55 and 255
                    else
                        Math.random() * 65 // between 0 and 65
                    decoder.onBrightnessUpdate(brightness)
                }
                Thread.sleep(TIME_UNIT / divide.toLong())
            }
        }

        // Assert
        val expected = " $text"
        assert(decoder.text == expected) { "Expected '$expected' but got '${decoder.text}'" }
    }

    private fun prepareMorse(text: String): List<Morse.Signal> {
        val morse: MutableList<Morse.Signal> = mutableListOf()
        for (i in 0 until text.length) {
            if (text[i] == ' ') morse.add(Morse.Signal.SPACE_WORDS)
            else {
                val letter = Morse.Letter.entries.first { it.char == text[i].uppercase() }
                for (i in 0 until letter.signals.size) {
                    morse.add(letter.signals[i])
                    if (i < letter.signals.size - 1) morse.add(Morse.Signal.SPACE_SIGNALS)
                    else morse.add(Morse.Signal.SPACE_LETTERS)
                }
            }
        }
        return morse
    }
}
