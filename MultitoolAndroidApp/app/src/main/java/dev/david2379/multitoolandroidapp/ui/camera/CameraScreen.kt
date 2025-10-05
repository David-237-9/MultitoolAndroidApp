package dev.david2379.multitoolandroidapp.ui.camera

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(onPreviewReady: (PreviewView) -> Unit) {
    Scaffold { innerPadding ->
        val context = LocalContext.current
        val scope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    val previewView = PreviewView(context)
                    scope.launch(Dispatchers.Main) {
                        onPreviewReady(previewView)
                    }
                    previewView
                }
            )
        }
    }
}
