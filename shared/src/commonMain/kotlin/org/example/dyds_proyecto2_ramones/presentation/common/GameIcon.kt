package org.example.dyds_proyecto2_ramones.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
fun GameIcon(
    iconUrl: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    fallbackTextColor: Color,
) {
    Box(
        modifier = modifier.background(backgroundColor),
        contentAlignment = Alignment.Center,
    ) {
        if (iconUrl.isBlank()) {
            Text(text = "IMG", color = fallbackTextColor, fontSize = 10.sp)
        } else {
            KamelImage(
                resource = asyncPainterResource(data = iconUrl),
                contentDescription = "Icono del juego",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

