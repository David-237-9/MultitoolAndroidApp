package dev.david2379.multitoolandroidapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.david2379.multitoolandroidapp.R

val appNameTextColor = Color.White
val buttonsTextColor = appNameTextColor
val appNameBackgroundColor = Color(215, 0, 0)
val buttonsBackgroundColor = Color(160, 160, 160)

@Composable
fun MainScreen(navigates: List<Pair<String, () -> Unit>>) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() }
    ) { innerPadding ->
        Box {
            Image(
                painter = painterResource(R.drawable.tools),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(navigates.size) { index ->
                    val (label, onClick) = navigates[index]
                    Button(
                        onClick = onClick,
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonColors(
                            buttonsBackgroundColor,
                            buttonsTextColor,
                            Color.DarkGray,
                            Color.Gray
                        ),
                    ) {
                        Text(label)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(appNameBackgroundColor)
                .padding(8.dp)
        ) {
            Text(
                text = "Multitool Android App",
                color = appNameTextColor,
            )
        }
    }
}
