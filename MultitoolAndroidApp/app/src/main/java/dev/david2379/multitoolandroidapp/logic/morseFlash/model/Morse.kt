package dev.david2379.multitoolandroidapp.logic.morseFlash.model

import kotlinx.coroutines.delay

const val TIME_UNIT = 300
const val TIME_BETWEEN_SIGNALS = TIME_UNIT
const val TIME_BETWEEN_LETTERS = TIME_UNIT * 3
const val TIME_BETWEEN_WORDS = TIME_UNIT * 7

object Morse {
    enum class Signal(millis: Int) {
        Dot(TIME_UNIT),
        Dash(TIME_UNIT * 3),
        SPACE_WORDS(TIME_BETWEEN_WORDS),
        SPACE_LETTERS(TIME_BETWEEN_LETTERS),
        SPACE_SIGNALS(TIME_BETWEEN_SIGNALS),
        ;
        val time = millis
    }

    enum class Letter(list: List<Signal>, symbol: String) {
        ONE(listOf(Signal.Dot, Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dash), "1"),
        TWO(listOf(Signal.Dot, Signal.Dot, Signal.Dash, Signal.Dash, Signal.Dash), "2"),
        THREE(listOf(Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dash, Signal.Dash), "3"),
        FOUR(listOf(Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dash), "4"),
        FIVE(listOf(Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dot), "5"),
        SIX(listOf(Signal.Dash, Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dot), "6"),
        SEVEN(listOf(Signal.Dash, Signal.Dash, Signal.Dot, Signal.Dot, Signal.Dot), "7"),
        EIGHT(listOf(Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dot, Signal.Dot), "8"),
        NINE(listOf(Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dot), "9"),
        ZERO(listOf(Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dash, Signal.Dash), "0"),
        A(listOf(Signal.Dot, Signal.Dash), "A"),
        B(listOf(Signal.Dash, Signal.Dot, Signal.Dot, Signal.Dot), "B"),
        C(listOf(Signal.Dash, Signal.Dot, Signal.Dash, Signal.Dot), "C"),
        D(listOf(Signal.Dash, Signal.Dot, Signal.Dot), "D"),
        E(listOf(Signal.Dot), "E"),
        F(listOf(Signal.Dot, Signal.Dot, Signal.Dash, Signal.Dot), "F"),
        G(listOf(Signal.Dash, Signal.Dash, Signal.Dot), "G"),
        H(listOf(Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dot), "H"),
        I(listOf(Signal.Dot, Signal.Dot), "I"),
        J(listOf(Signal.Dot, Signal.Dash, Signal.Dash, Signal.Dash), "J"),
        K(listOf(Signal.Dash, Signal.Dot, Signal.Dash), "K"),
        L(listOf(Signal.Dot, Signal.Dash, Signal.Dot, Signal.Dot), "L"),
        M(listOf(Signal.Dash, Signal.Dash), "M"),
        N(listOf(Signal.Dash, Signal.Dot), "N"),
        O(listOf(Signal.Dash, Signal.Dash, Signal.Dash), "O"),
        P(listOf(Signal.Dot, Signal.Dash, Signal.Dash, Signal.Dot), "P"),
        Q(listOf(Signal.Dash, Signal.Dash, Signal.Dot, Signal.Dash), "Q"),
        R(listOf(Signal.Dot, Signal.Dash, Signal.Dot), "R"),
        S(listOf(Signal.Dot, Signal.Dot, Signal.Dot), "S"),
        T(listOf(Signal.Dash), "T"),
        U(listOf(Signal.Dot, Signal.Dot, Signal.Dash), "U"),
        V(listOf(Signal.Dot, Signal.Dot, Signal.Dot, Signal.Dash), "V"),
        W(listOf(Signal.Dot, Signal.Dash, Signal.Dash), "W"),
        X(listOf(Signal.Dash, Signal.Dot, Signal.Dot, Signal.Dash), "X"),
        Y(listOf(Signal.Dash, Signal.Dot, Signal.Dash, Signal.Dash), "Y"),
        Z(listOf(Signal.Dash, Signal.Dash, Signal.Dot, Signal.Dot), "Z"),
        DOT(listOf(Signal.Dot, Signal.Dash, Signal.Dot, Signal.Dash, Signal.Dot, Signal.Dash), "."),
        COMMA(listOf(Signal.Dash, Signal.Dash, Signal.Dot, Signal.Dot, Signal.Dash, Signal.Dash), ","),
        QUESTION(listOf(Signal.Dot, Signal.Dot, Signal.Dash, Signal.Dash, Signal.Dot), "?"),
        ;
        val signals = list
        val char = symbol
    }

    private val charToMorse = mapOf(
        '1' to Letter.ONE,
        '2' to Letter.TWO,
        '3' to Letter.THREE,
        '4' to Letter.FOUR,
        '5' to Letter.FIVE,
        '6' to Letter.SIX,
        '7' to Letter.SEVEN,
        '8' to Letter.EIGHT,
        '9' to Letter.NINE,
        '0' to Letter.ZERO,
        'A' to Letter.A,
        'B' to Letter.B,
        'C' to Letter.C,
        'D' to Letter.D,
        'E' to Letter.E,
        'F' to Letter.F,
        'G' to Letter.G,
        'H' to Letter.H,
        'I' to Letter.I,
        'J' to Letter.J,
        'K' to Letter.K,
        'L' to Letter.L,
        'M' to Letter.M,
        'N' to Letter.N,
        'O' to Letter.O,
        'P' to Letter.P,
        'Q' to Letter.Q,
        'R' to Letter.R,
        'S' to Letter.S,
        'T' to Letter.T,
        'U' to Letter.U,
        'V' to Letter.V,
        'W' to Letter.W,
        'X' to Letter.X,
        'Y' to Letter.Y,
        'Z' to Letter.Z,
        '.' to Letter.DOT,
        ',' to Letter.COMMA,
        '?' to Letter.QUESTION,
    )

    /**
     * Flash the given code in Morse.
     * @param code The code to flash.
     * @param on Function to turn on the flash.
     * @param off Function to turn off the flash.
     * @param stop Function to check if the flashing should stop.
     */
    suspend fun flashCode(code: String, on: () -> Unit, off: () -> Unit, stop: () -> Boolean) {
        for (char in code) {
            if (char == ' ')
                delay(TIME_BETWEEN_WORDS - TIME_BETWEEN_LETTERS.toLong())
            else {
                val morse = charToMorse[char.uppercaseChar()] ?: continue // skip unknown characters
                for (signal in morse.signals) {
                    on()
                    delay(signal.time.toLong())
                    off()
                    delay(TIME_BETWEEN_SIGNALS.toLong())
                }
            }
            if (stop()) break
        }
    }
}
