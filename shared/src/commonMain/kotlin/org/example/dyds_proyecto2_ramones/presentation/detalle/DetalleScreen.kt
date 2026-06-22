package org.example.dyds_proyecto2_ramones.presentation.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.launch
import org.example.dyds_proyecto2_ramones.domain.model.DetalleJuego
import org.example.dyds_proyecto2_ramones.presentation.common.formatHoursOneDecimal
import org.example.dyds_proyecto2_ramones.presentation.common.GameIcon
import org.example.dyds_proyecto2_ramones.presentation.common.UiState
import coil3.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@Composable
fun DetalleScreen(
    steamId: String,
    appId: String,
    onNavigateBack: () -> Unit,
    viewModel: DetalleViewModel,
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

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val esFavorito by viewModel.esFavorito.collectAsState()
    val descripcionTraducida by viewModel.descripcionTraducida.collectAsState()

    LaunchedEffect(steamId, appId) {
        viewModel.cargarDetalle(steamId, appId)
        //viewModel.traducirDescripcionActual()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBase)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        when (val state = uiState) {
            is UiState.Idle, UiState.Loading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accent)
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, border, RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(state.message, color = textMuted, fontSize = 13.sp, textAlign = TextAlign.Center)
                    Button(
                        onClick = { scope.launch { viewModel.reintentar() } },
                        colors = ButtonDefaults.buttonColors(containerColor = elevated, contentColor = textPrim),
                        border = androidx.compose.foundation.BorderStroke(1.dp, border),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Reintentar", fontSize = 12.sp)
                    }
                }
            }

            is UiState.Success -> {
                DetalleContent(
                    detalle = state.data,
                    esFavorito = esFavorito,
                    descripcionTraducida = descripcionTraducida,
                    bgSurface = bgSurface,
                    elevated = elevated,
                    accent = accent,
                    accentDim = accentDim,
                    textPrim = textPrim,
                    textMuted = textMuted,
                    border = border,
                    danger = danger,
                    onToggleFavorito = { scope.launch { viewModel.toggleFavorito() } },
                )
            }
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
private fun DetalleContent(
    detalle: DetalleJuego,
    esFavorito: Boolean,
    descripcionTraducida: String?,
    bgSurface: Color,
    elevated: Color,
    accent: Color,
    accentDim: Color,
    textPrim: Color,
    textMuted: Color,
    border: Color,
    danger: Color,
    onToggleFavorito: () -> Unit,
) {
    val rawgNoDisponible = detalle.descripcion.isBlank() &&
        detalle.metacriticScore == null &&
        detalle.screenshots.isEmpty()

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
            GameIcon(
                iconUrl = detalle.juego.iconUrl,
                modifier = Modifier.fillMaxSize(),
                backgroundColor = elevated,
                fallbackTextColor = textMuted,
            )
        }

        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = detalle.juego.nombre,
                    color = textPrim,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Button(
                    onClick = onToggleFavorito,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (esFavorito) Color(0x14E2574C) else bgSurface,
                        contentColor = if (esFavorito) danger else textMuted,
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (esFavorito) danger else border,
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    HeartIcon(active = esFavorito, tint = if (esFavorito) danger else textMuted)
                }
            }

            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(detalle.juego.generos.ifEmpty { listOf("Sin genero") }) { genero ->
                    Box(
                        modifier = Modifier
                            .background(accentDim, RoundedCornerShape(20.dp))
                            .border(1.dp, accent, RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Text(text = genero, color = accent, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        StatCard(
            modifier = Modifier.weight(1f),
            label = "APP ID",
            value = detalle.juego.appId,
            valueColor = textPrim,
            bgSurface = bgSurface,
            border = border,
            textMuted = textMuted,
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = "HORAS",
            value = "${detalle.juego.horasJugadas.formatHoursOneDecimal()}h",
            valueColor = textPrim,
            bgSurface = bgSurface,
            border = border,
            textMuted = textMuted,
        )
        StatCard(
            modifier = Modifier.weight(1f),
            label = "METACRITIC",
            value = detalle.metacriticScore?.toString() ?: "--",
            valueColor = if (detalle.metacriticScore != null) accent else textMuted,
            bgSurface = bgSurface,
            border = border,
            textMuted = textMuted,
        )
    }

    if (rawgNoDisponible) {
        Text(
            text = "No se encontraron datos extra en RAWG. Mostrando datos de Steam.",
            color = textMuted,
            fontSize = 12.sp,
        )
    }

    if (detalle.screenshots.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "SCREENSHOTS",
                color = textMuted,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp,
                fontWeight = FontWeight.Normal,
            )

            val pagerState = rememberPagerState(pageCount = { detalle.screenshots.size })
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 4.dp),
                pageSpacing = 8.dp,
                modifier = Modifier.fillMaxWidth().heightIn(min = 84.dp),
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bgSurface, RoundedCornerShape(8.dp))
                        .border(1.dp, border, RoundedCornerShape(8.dp))
                        .padding(12.dp),
                ) {
                    AsyncImage(
                        model = detalle.screenshots[page],
                        contentDescription = "Screenshot ${page + 1}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(detalle.screenshots.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                            .background(if (pagerState.currentPage == index) accent else border, RoundedCornerShape(50))
                            .clickable { }
                    )
                }
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "LOGROS",
            color = textMuted,
            fontSize = 11.sp,
            letterSpacing = 0.8.sp,
            fontWeight = FontWeight.Normal,
        )

        if (detalle.logros.isEmpty()) {
            Text("No hay logros disponibles", color = textMuted, fontSize = 12.sp)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                detalle.logros.forEach { logro ->
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
                            Text(if (logro.conseguido) "✓" else "", fontSize = 12.sp, color = accent)
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(logro.nombre, color = textPrim, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            Text(logro.descripcion, color = textMuted, fontSize = 11.sp)
                        }

                        if (logro.conseguido) {
                            CheckIcon(tint = accent)
                        }
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
            text = descripcionTraducida ?: detalle.descripcion.ifBlank { "Sin descripcion disponible" },
            color = textMuted,
            fontSize = 13.sp,
            lineHeight = 22.sp,
        )
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

