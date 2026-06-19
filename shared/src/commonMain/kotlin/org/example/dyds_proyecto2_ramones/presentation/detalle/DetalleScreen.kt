package org.example.dyds_proyecto2_ramones.presentation.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.launch
import org.example.dyds_proyecto2_ramones.domain.model.Juego
import org.example.dyds_proyecto2_ramones.domain.usecase.AgregarFavoritoUseCase
import org.example.dyds_proyecto2_ramones.domain.usecase.EliminarFavoritoUseCase

@Composable
fun DetalleScreen(
    appId: String,
    onNavigateFavoritos: () -> Unit,
    onNavigateBack: () -> Unit,
    agregarFavoritoUseCase: AgregarFavoritoUseCase,
    eliminarFavoritoUseCase: EliminarFavoritoUseCase,
) {
    val bgBase = Color(0xFF0E1117)
    val bgSurface = Color(0xFF161B27)
    val elevated = Color(0xFF1E2537)
    val accent = Color(0xFF4D9FFF)
    val accentDim = Color(0xFF1A3A5C)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)
    val danger = Color(0xFFE2574C)

    val gameName = "Counter-Strike 2"
    val genres = listOf("Shooter", "Accion", "Competitivo")
    val description = "Shooter tactico por equipos con economia de rondas, enfoque competitivo y una escena de esports activa. Domina mapas, utilidades y comunicacion para subir de rango."
    val logros = listOf(
        Triple("Primera Sangre", "Consigue tu primer asesinato", true),
        Triple("Economia", "Gana 10 rondas ahorrando", false),
        Triple("Ace", "Elimina a los 5 enemigos en una ronda", false),
        Triple("Granada precisa", "Consigue 25 bajas con granada", true),
        Triple("Invencible", "Gana 5 partidas seguidas", false),
    )

    var favoritoActivo by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBase)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(elevated, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Text("")
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = gameName,
                        color = textPrim,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Button(
                        onClick = {
                            favoritoActivo = !favoritoActivo
                            scope.launch {
                                val juego = Juego(appId, gameName, 310.0, "", genres)
                                if (favoritoActivo) {
                                    agregarFavoritoUseCase.invoke(juego)
                                } else {
                                    eliminarFavoritoUseCase.invoke(appId)
                                }
                                onNavigateFavoritos()
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (favoritoActivo) Color(0x14E2574C) else bgSurface,
                            contentColor = if (favoritoActivo) danger else textMuted,
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (favoritoActivo) danger else border,
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                    ) {
                        HeartIcon(active = favoritoActivo, tint = if (favoritoActivo) danger else textMuted)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    genres.forEach { genre ->
                        Box(
                            modifier = Modifier
                                .background(accentDim, RoundedCornerShape(20.dp))
                                .border(1.dp, accent, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                        ) {
                            Text(text = genre, color = accent, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "APP ID",
                value = appId,
                valueColor = textPrim,
                bgSurface = bgSurface,
                border = border,
                textMuted = textMuted,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "HORAS",
                value = "310h",
                valueColor = textPrim,
                bgSurface = bgSurface,
                border = border,
                textMuted = textMuted,
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "METACRITIC",
                value = "84",
                valueColor = accent,
                bgSurface = bgSurface,
                border = border,
                textMuted = textMuted,
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "LOGROS",
                color = textMuted,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.Normal,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                logros.forEach { logro ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgSurface, RoundedCornerShape(8.dp))
                            .border(1.dp, border, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(elevated, RoundedCornerShape(4.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("", fontSize = 12.sp)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(logro.first, color = textPrim, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text(logro.second, color = textMuted, fontSize = 11.sp)
                        }

                        if (logro.third) {
                            CheckIcon(tint = accent)
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(bgSurface, RoundedCornerShape(8.dp))
                .border(1.dp, border, RoundedCornerShape(8.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "DESCRIPCION",
                color = textMuted,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.Normal,
            )
            Text(
                text = description,
                color = textMuted,
                fontSize = 13.sp,
                lineHeight = 22.sp,
            )
        }

        Button(
            onClick = onNavigateBack,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = textPrim),
            border = androidx.compose.foundation.BorderStroke(1.dp, border),
        ) {
            Text("Volver")
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    valueColor: Color,
    bgSurface: Color,
    border: Color,
    textMuted: Color,
) {
    Column(
        modifier = modifier
            .background(bgSurface, RoundedCornerShape(8.dp))
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(label, color = textMuted, fontSize = 11.sp)
        Text(value, color = valueColor, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun HeartIcon(active: Boolean, tint: Color) {
    Canvas(modifier = Modifier.size(16.dp)) {
        val stroke = 1.8f
        val left = Offset(size.width * 0.3f, size.height * 0.35f)
        val right = Offset(size.width * 0.7f, size.height * 0.35f)
        if (active) {
            drawCircle(color = tint, radius = size.width * 0.18f, center = left)
            drawCircle(color = tint, radius = size.width * 0.18f, center = right)
            drawLine(color = tint, start = Offset(size.width * 0.15f, size.height * 0.42f), end = Offset(size.width * 0.5f, size.height * 0.85f), strokeWidth = stroke)
            drawLine(color = tint, start = Offset(size.width * 0.85f, size.height * 0.42f), end = Offset(size.width * 0.5f, size.height * 0.85f), strokeWidth = stroke)
        } else {
            drawCircle(color = tint, radius = size.width * 0.18f, center = left, style = Stroke(width = stroke))
            drawCircle(color = tint, radius = size.width * 0.18f, center = right, style = Stroke(width = stroke))
            drawLine(color = tint, start = Offset(size.width * 0.15f, size.height * 0.42f), end = Offset(size.width * 0.5f, size.height * 0.85f), strokeWidth = stroke)
            drawLine(color = tint, start = Offset(size.width * 0.85f, size.height * 0.42f), end = Offset(size.width * 0.5f, size.height * 0.85f), strokeWidth = stroke)
        }
    }
}

@Composable
private fun CheckIcon(tint: Color) {
    Canvas(modifier = Modifier.size(14.dp)) {
        drawLine(tint, Offset(size.width * 0.1f, size.height * 0.55f), Offset(size.width * 0.4f, size.height * 0.85f), strokeWidth = 1.8f)
        drawLine(tint, Offset(size.width * 0.4f, size.height * 0.85f), Offset(size.width * 0.9f, size.height * 0.15f), strokeWidth = 1.8f)
    }
}

