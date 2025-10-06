package dev.david2379.multitoolandroidapp.logic.morseFlash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import dev.david2379.multitoolandroidapp.logic.morseFlash.model.FlashHelper
import dev.david2379.multitoolandroidapp.logic.morseFlash.model.Morse
import dev.david2379.multitoolandroidapp.ui.morseFlash.MorseFlashScreen
import dev.david2379.multitoolandroidapp.ui.theme.MultitoolAndroidAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MorseFlashActivity: ComponentActivity() {
    private var morseJob: Job? = null
    private val flashHelper by lazy { FlashHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var state by remember { mutableStateOf(Pair("", false)) }

            fun startMorseJob() {
                morseJob?.cancel()
                morseJob = lifecycleScope.launch(Dispatchers.Default) {
                    if (state.second) Morse.flashCode(
                        state.first,
                        { flashHelper.turnOnFlash() },
                        { flashHelper.turnOffFlash() },
                        { !state.second },
                    ) else morseJob?.cancel()
                }
            }

            fun setState(text: String, active: Boolean) {
                state = Pair(text.trim(), active)
                if (active && state.first.isNotBlank()) startMorseJob() else {
                    morseJob?.cancel()
                    flashHelper.turnOffFlash()
                }
            }

            MultitoolAndroidAppTheme {
                MorseFlashScreen(
                    "Morse Flash Tool"
                ) { text, active -> setState(text, active) }
            }
        }
    }
}
