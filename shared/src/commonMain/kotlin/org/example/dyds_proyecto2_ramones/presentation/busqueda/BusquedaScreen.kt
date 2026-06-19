package org.example.dyds_proyecto2_ramones.presentation.busqueda

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.input.ImeAction

@Composable
fun BusquedaScreen(
    onNavigateBiblioteca: (String) -> Unit,
    onNavigateFavoritos: () -> Unit,
) {
    val bgBase = Color(0xFF0E1117)
    val bgSurface = Color(0xFF161B27)
    val accent = Color(0xFF4D9FFF)
    val textPrim = Color(0xFFE8EAF0)
    val textMuted = Color(0xFF7A8599)
    val border = Color(0xFF242D42)
    val danger = Color(0xFFE2574C)

    var steamIdInput by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    fun onSearch() {
        val value = steamIdInput.trim()
        if (value.isEmpty()) {
            showError = true
            return
        }

        showError = false
        onNavigateBiblioteca(value)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBase)
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
                .align(Alignment.Center)
                .background(bgSurface, RoundedCornerShape(8.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tu perfil Steam,",
                    color = textPrim,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp,
                )
                Text(
                    text = "en un vistazo",
                    color = accent,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 34.sp,
                )
            }

            Text(
                text = "Ingresa tu SteamID para ver tu biblioteca, logros y estadisticas",
                color = textMuted,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center,
            )

            OutlinedTextField(
                value = steamIdInput,
                onValueChange = {
                    steamIdInput = it
                    if (showError && it.isNotBlank()) {
                        showError = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = "SteamID o URL de perfil",
                        color = textMuted,
                        fontSize = 13.sp,
                    )
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = textPrim,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                ),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textPrim,
                    unfocusedTextColor = textPrim,
                    focusedContainerColor = bgSurface,
                    unfocusedContainerColor = bgSurface,
                    focusedBorderColor = accent,
                    unfocusedBorderColor = border,
                    cursorColor = accent,
                    focusedPlaceholderColor = textMuted,
                    unfocusedPlaceholderColor = textMuted,
                ),
            )

            Button(
                onClick = { onSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accent,
                    contentColor = Color.White,
                ),
                border = BorderStroke(1.dp, accent),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    SearchOutlineIcon(tint = Color.White)
                    Text(
                        text = "Buscar",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            if (showError) {
                Text(
                    text = "Ingresa un SteamID para continuar",
                    color = danger,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Text(
                text = "Ejemplo: 76561198012345678",
                color = textMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SearchOutlineIcon(tint: Color) {
    Canvas(modifier = Modifier.size(18.dp)) {
        val radius = size.minDimension * 0.3f
        val center = androidx.compose.ui.geometry.Offset(size.width * 0.43f, size.height * 0.43f)
        drawCircle(
            color = tint,
            radius = radius,
            center = center,
            style = Stroke(width = 2f),
        )
        drawLine(
            color = tint,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.63f, size.height * 0.63f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.9f, size.height * 0.9f),
            strokeWidth = 2f,
        )
    }
}

