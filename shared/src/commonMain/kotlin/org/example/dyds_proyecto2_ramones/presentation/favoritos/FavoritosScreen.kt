package org.example.dyds_proyecto2_ramones.presentation.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.dyds_proyecto2_ramones.domain.model.Juego

@Composable
fun FavoritosScreen(
    onNavigateDetalle: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val bgBase = Color(0xFF0E1117)
    val bgSurface = Color(0xFF161B27)
    val elevated = Color(0xFF1E2537)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)

    var favoritos by remember {
        mutableStateOf(
            listOf(
                Juego("570", "Dota 2", 124.5, "", listOf("Estrategia")),
                Juego("39210", "Final Fantasy XIV", 203.2, "", listOf("RPG")),
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBase)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = "Favoritos",
            color = textPrim,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = "Juegos guardados para acceso rapido",
            color = textMuted,
            fontSize = 13.sp,
        )

        if (favoritos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = "💔✕",
                        fontSize = 40.sp,
                        modifier = Modifier.alpha(0.4f),
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "No hay favoritos aun",
                        color = textMuted,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "Agrega juegos desde el detalle",
                        color = textMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(favoritos) { juego ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgSurface, RoundedCornerShape(8.dp))
                            .border(1.dp, border, RoundedCornerShape(8.dp))
                            .clickable { onNavigateDetalle(juego.appId) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(elevated, RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("🎮")
                        }

                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = juego.nombre,
                                color = textPrim,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                            )
                            Text(
                                text = "${juego.generos.firstOrNull() ?: "Sin genero"} • ${juego.horasJugadas} h",
                                color = textMuted,
                                fontSize = 12.sp,
                            )
                        }

                        Button(
                            onClick = {
                                favoritos = favoritos.filter { it.appId != juego.appId }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = textMuted,
                            ),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp),
                        ) {
                            Text("✕", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
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

