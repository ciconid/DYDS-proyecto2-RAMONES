package org.example.dyds_proyecto2_ramones.presentation.biblioteca

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BibliotecaScreen(
    steamId: String,
    onNavigateDetalle: (String) -> Unit,
    onNavigateFavoritos: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BibliotecaViewModel,
) {
    val bgBase = Color(0xFF0E1117)
    val bgSurface = Color(0xFF161B27)
    val elevated = Color(0xFF1E2537)
    val accent = Color(0xFF4D9FFF)
    val accentDim = Color(0xFF1A3A5C)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)

    val scope = rememberCoroutineScope()
    var activeAppId by remember { mutableStateOf<String?>(null) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(steamId) {
        viewModel.cargarBiblioteca(steamId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBase)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Biblioteca", color = textPrim, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = "Perfil ${uiState.steamId.ifBlank { steamId }} • ${uiState.juegosVisibles.size} juegos",
                    color = textMuted,
                    fontSize = 13.sp,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onNavigateBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = textPrim),
                    border = BorderStroke(1.dp, border),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    Text("Volver", fontSize = 12.sp, fontWeight = FontWeight.Normal)
                }

                Button(
                    onClick = onNavigateFavoritos,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = textPrim),
                    border = BorderStroke(1.dp, border),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    Text("Favoritos", fontSize = 12.sp, fontWeight = FontWeight.Normal)
                }

                Button(
                    onClick = { viewModel.toggleOrden() },
                    colors = ButtonDefaults.buttonColors(containerColor = elevated, contentColor = textPrim),
                    border = BorderStroke(1.dp, border),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp),
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                        SortOutlineIcon(tint = textPrim)
                        Text("Horas", fontSize = 12.sp, fontWeight = FontWeight.Normal)
                    }
                }
            }
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            uiState.generosDisponibles.forEach { genre ->
                val isActive = genre == uiState.selectedGenero
                Box(
                    modifier = Modifier
                        .background(if (isActive) accentDim else bgSurface, RoundedCornerShape(20.dp))
                        .border(1.dp, if (isActive) accent else border, RoundedCornerShape(20.dp))
                        .clickable { viewModel.seleccionarGenero(genre) }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                ) {
                    Text(genre, color = if (isActive) accent else textMuted, fontSize = 12.sp)
                }
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = accent)
            }
        } else if (!uiState.errorMessage.isNullOrBlank()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = uiState.errorMessage ?: "Error al cargar biblioteca",
                        color = textMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.reintentar()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = elevated, contentColor = textPrim),
                        border = BorderStroke(1.dp, border),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Reintentar", fontSize = 12.sp)
                    }
                }
            }
        } else if (uiState.juegosVisibles.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("🎮", fontSize = 42.sp, modifier = Modifier.alpha(0.4f))
                    Text("Sin juegos en esta categoria", color = textMuted, fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(uiState.juegosVisibles) { game ->
                    val isActive = activeAppId == game.appId
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) elevated else Color.Transparent)
                            .clickable {
                                activeAppId = game.appId
                                onNavigateDetalle(game.appId)
                            }
                            .padding(vertical = 10.dp, horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 3.dp, height = 44.dp)
                                .background(if (isActive) accent else Color.Transparent),
                        )

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(elevated, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("🎮")
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(game.nombre, color = textPrim, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            Text(
                                text = "${game.generos.firstOrNull() ?: "Sin genero"} • ${game.horasJugadas} h",
                                color = textMuted,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SortOutlineIcon(tint: Color) {
    Canvas(modifier = Modifier.size(14.dp)) {
        drawLine(tint, Offset(2f, 3f), Offset(size.width - 2f, 3f), strokeWidth = 1.8f)
        drawLine(tint, Offset(4f, size.height / 2f), Offset(size.width - 4f, size.height / 2f), strokeWidth = 1.8f)
        drawLine(tint, Offset(6f, size.height - 3f), Offset(size.width - 6f, size.height - 3f), strokeWidth = 1.8f)
        drawCircle(tint, radius = 1.2f, center = Offset(3f, 3f), style = Stroke(width = 1.2f))
    }
}

