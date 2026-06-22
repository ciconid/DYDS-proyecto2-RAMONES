package org.example.dyds_proyecto2_ramones.presentation.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset

sealed class Screen {
    object Busqueda : Screen()
    object Biblioteca : Screen()
    data class Detalle(val appId: String, val fromFavoritos: Boolean = false) : Screen()
    object Favoritos : Screen()
}

@Composable
fun NavigationRail(
    expanded: Boolean,
    onToggle: () -> Unit,
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
) {
    val bgSurface = Color(0xFF161B27)
    val elevated = Color(0xFF1E2537)
    val accent = Color(0xFF4D9FFF)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)

    val animatedWidth = animateDpAsState(if (expanded) 200.dp else 64.dp)

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(animatedWidth.value)
            .background(bgSurface)
            .border(width = 1.dp, color = border)
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle)
                .padding(horizontal = 20.dp, vertical = 9.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MenuIcon(tint = textMuted)
            if (expanded) {
                Text(
                    text = "Menu",
                    color = textMuted,
                    fontSize = 13.sp,
                )
            }
        }

        NavigationRailItem(
            icon = { SearchIcon(it) },
            label = "Busqueda",
            expanded = expanded,
            active = currentScreen is Screen.Busqueda,
            onClick = { onNavigate(Screen.Busqueda) },
            textPrim = textPrim,
            textMuted = textMuted,
            elevated = elevated,
            accent = accent,
        )

        NavigationRailItem(
            icon = { BooksIcon(it) },
            label = "Biblioteca",
            expanded = expanded,
            active = currentScreen is Screen.Biblioteca,
            onClick = { onNavigate(Screen.Biblioteca) },
            textPrim = textPrim,
            textMuted = textMuted,
            elevated = elevated,
            accent = accent,
        )

        NavigationRailItem(
            icon = { HeartIcon(it) },
            label = "Favoritos",
            expanded = expanded,
            active = currentScreen is Screen.Favoritos,
            onClick = { onNavigate(Screen.Favoritos) },
            textPrim = textPrim,
            textMuted = textMuted,
            elevated = elevated,
            accent = accent,
        )
    }
}

@Composable
private fun NavigationRailItem(
    icon: @Composable (Color) -> Unit,
    label: String,
    expanded: Boolean,
    active: Boolean,
    onClick: () -> Unit,
    textPrim: Color,
    textMuted: Color,
    elevated: Color,
    accent: Color,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (active) elevated else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 9.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 3.dp, height = 24.dp)
                .background(if (active) accent else Color.Transparent),
        )

        icon(if (active) textPrim else textMuted)

        if (expanded) {
            Text(
                text = label,
                color = if (active) textPrim else textMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}

@Composable
private fun MenuIcon(tint: Color) {
    Canvas(modifier = Modifier.size(16.sp.value.dp)) {
        drawLine(tint, Offset(2f, 4f), Offset(size.width - 2f, 4f), strokeWidth = 1.8f)
        drawLine(tint, Offset(2f, size.height / 2f), Offset(size.width - 2f, size.height / 2f), strokeWidth = 1.8f)
        drawLine(tint, Offset(2f, size.height - 4f), Offset(size.width - 2f, size.height - 4f), strokeWidth = 1.8f)
    }
}

@Composable
private fun SearchIcon(tint: Color) {
    Canvas(modifier = Modifier.size(16.sp.value.dp)) {
        drawCircle(tint, radius = size.minDimension * 0.3f, center = Offset(size.width * 0.4f, size.height * 0.4f), style = Stroke(width = 1.8f))
        drawLine(tint, Offset(size.width * 0.6f, size.height * 0.6f), Offset(size.width * 0.85f, size.height * 0.85f), strokeWidth = 1.8f)
    }
}

@Composable
private fun BooksIcon(tint: Color) {
    Canvas(modifier = Modifier.size(16.sp.value.dp)) {
        drawRect(tint, topLeft = Offset(1f, 2f), size = androidx.compose.ui.geometry.Size(5f, 10f), style = Stroke(width = 1.6f))
        drawRect(tint, topLeft = Offset(8f, 2f), size = androidx.compose.ui.geometry.Size(5f, 10f), style = Stroke(width = 1.6f))
        drawLine(tint, Offset(6.5f, 2f), Offset(6.5f, 12f), strokeWidth = 1.6f)
    }
}

@Composable
private fun HeartIcon(tint: Color) {
    Canvas(modifier = Modifier.size(16.sp.value.dp)) {
        val left = Offset(size.width * 0.3f, size.height * 0.35f)
        val right = Offset(size.width * 0.7f, size.height * 0.35f)
        drawCircle(tint, radius = size.width * 0.15f, center = left, style = Stroke(width = 1.6f))
        drawCircle(tint, radius = size.width * 0.15f, center = right, style = Stroke(width = 1.6f))
        drawLine(tint, Offset(size.width * 0.15f, size.height * 0.42f), Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = 1.6f)
        drawLine(tint, Offset(size.width * 0.85f, size.height * 0.42f), Offset(size.width * 0.5f, size.height * 0.8f), strokeWidth = 1.6f)
    }
}


