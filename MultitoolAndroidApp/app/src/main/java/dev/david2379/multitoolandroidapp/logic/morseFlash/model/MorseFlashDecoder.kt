package dev.david2379.multitoolandroidapp.logic.morseFlash.model

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class MorseFlashDecoder {
    var text = ""
        private set
    private var lastState = false
    private var changesTimeList = listOf<Long>(0L)
    private val lock = ReentrantLock()


    fun onBrightnessUpdate(brightness: Double) {
        val isOn = isOn(brightness)
        if (isOn == lastState) return // No state change

//        val lastIdx = changesTimeList.size - 1
//        // State changed with error correction
//        if (changesTimeList.size < 2) {
//            changesTimeList = changesTimeList + System.currentTimeMillis()
//            lastState = isOn
//            return
//        }
//        if (changesTimeList[lastIdx] - changesTimeList[lastIdx-1] < TIME_UNIT / 4) {
//            changesTimeList = changesTimeList.dropLast(1)
//            return
//        }

        // Valid state change
        val currentTime = System.currentTimeMillis()
        lock.withLock { onStateChange(isOn, currentTime) }
    }



    private fun onStateChange(isOn: Boolean, currentTime: Long) {
        changesTimeList = changesTimeList + currentTime
        val oldestChange = if (changesTimeList.size % 2 == 0) lastState else !lastState

        val listOfStates = mutableListOf<Pair<Boolean, Long>>()
        for (i in 1 until changesTimeList.size) {
            val state = if (i % 2 != 0) oldestChange else !oldestChange
            listOfStates.add(state to (changesTimeList[i] - changesTimeList[i-1]))
        }

        lastState = isOn

        translateToLetters(translateToSignals(listOfStates))
    }

    private fun translateToSignals(listOfStates: List<Pair<Boolean, Long>>): List<Morse.Signal> {
        val list = mutableListOf<Morse.Signal>()
        for ((state, time) in listOfStates) {
            if (time < TIME_UNIT * 2) {
                if (state) {
                    list.add(Morse.Signal.Dot)
                }
                else {
                    list.add(Morse.Signal.SPACE_SIGNALS)
                }
            }
            else if (time < TIME_UNIT * 4) {
                if (state) {
                    list.add(Morse.Signal.Dash)
                }
                else {
                    list.add(Morse.Signal.SPACE_LETTERS)
                }
            }
            else {
                list.add(Morse.Signal.SPACE_WORDS)
            }
        }
        return list
    }

    private fun translateToLetters(signals: List<Morse.Signal>) {
//        println("========================== TRANSLATING TO LETTERS ==========================")
        val separators = listOf(Morse.Signal.SPACE_LETTERS, Morse.Signal.SPACE_WORDS)
        text = ""
        for (i in 0 until signals.size) {
            if (signals[i] in separators) {
                if (signals[i] == Morse.Signal.SPACE_WORDS) text += ' '
                for (j in i + 1 until signals.size) {
                    if (signals[j] in separators || j == signals.size - 1) {
                        val subList = signals.subList(i + 1, j + 1)
//                        println("SUBLIST: " + subList)
                        for (letter in Morse.Letter.entries) {
                            if (letter.signals == subList.filter { it == Morse.Signal.Dot || it == Morse.Signal.Dash }) {
//                                println("FOUND LETTER: " + letter.char + " IN LIST: " + subList)
                                text += letter.char
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isOn(brightness: Double): Boolean = brightness > 60
}
