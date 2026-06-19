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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import org.example.dyds_proyecto2_ramones.domain.model.Juego

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BibliotecaScreen(
    steamId: String,
    onNavigateDetalle: (String) -> Unit,
    onNavigateFavoritos: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val bgBase = Color(0xFF0E1117)
    val bgSurface = Color(0xFF161B27)
    val elevated = Color(0xFF1E2537)
    val accent = Color(0xFF4D9FFF)
    val accentDim = Color(0xFF1A3A5C)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)

    val genres = listOf("Todos", "Accion", "RPG", "Estrategia", "Shooter")
    val games = remember {
        listOf(
            Juego("570", "Dota 2", 124.5, "", listOf("Estrategia")),
            Juego("730", "Counter-Strike 2", 310.0, "", listOf("Shooter", "Accion")),
            Juego("578080", "PUBG", 57.3, "", listOf("Shooter", "Accion")),
            Juego("1174180", "Red Dead Redemption 2", 88.0, "", listOf("RPG", "Accion")),
            Juego("39210", "Final Fantasy XIV", 203.2, "", listOf("RPG")),
        )
    }

    var selectedGenre by remember { mutableStateOf("Todos") }
    var sortDesc by remember { mutableStateOf(true) }
    var activeAppId by remember { mutableStateOf<String?>(null) }

    val filtered = games.filter {
        selectedGenre == "Todos" || it.generos.any { g -> g.equals(selectedGenre, ignoreCase = true) }
    }
    val visibleGames = if (sortDesc) filtered.sortedByDescending { it.horasJugadas } else filtered.sortedBy { it.horasJugadas }

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
                    text = "Perfil $steamId • ${games.size} juegos",
                    color = textMuted,
                    fontSize = 13.sp,
                )
            }

            Button(
                onClick = { sortDesc = !sortDesc },
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

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            genres.forEach { genre ->
                val isActive = genre == selectedGenre
                Box(
                    modifier = Modifier
                        .background(if (isActive) accentDim else bgSurface, RoundedCornerShape(20.dp))
                        .border(1.dp, if (isActive) accent else border, RoundedCornerShape(20.dp))
                        .clickable { selectedGenre = genre }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                ) {
                    Text(genre, color = if (isActive) accent else textMuted, fontSize = 12.sp)
                }
            }
        }

        if (visibleGames.isEmpty()) {
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
                items(visibleGames) { game ->
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

