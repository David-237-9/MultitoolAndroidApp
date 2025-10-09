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
        val repeat = 4
        val morse: MutableList<Morse.Signal> = mutableListOf()
        // Generate morse code
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

        for (signal in morse) {
            val brightness = if (signal == Morse.Signal.Dot || signal == Morse.Signal.Dash) 255.0 else 0.0
            repeat((signal.time * repeat) / TIME_UNIT) {
                scope.launch { decoder.onBrightnessUpdate(brightness) }
                Thread.sleep(TIME_UNIT / repeat.toLong())
            }
        }

        val expected = " $text"
        assert(decoder.text == expected) { "Expected '$expected' but got '${decoder.text}'" }
    }
}
