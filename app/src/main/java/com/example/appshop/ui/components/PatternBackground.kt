package com.example.appshop.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.appshop.R

@Composable
fun PatternBackground() {
    val iconSize = 25.dp
    val spacing = 15.dp
    val icons = listOf(
        painterResource(id = R.drawable.muffin),
        painterResource(id = R.drawable.cake)
    )
    // Añadimos una capa de color a los iconos, usando el color primario del tema.
    // El alpha se reduce para que el patrón sea más sutil como fondo.
    val iconColor = MaterialTheme.colorScheme.primary

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val iconPx = iconSize.toPx()
            val spacingPx = spacing.toPx()
            val step = iconPx + spacingPx
            val columns = (size.width / step).toInt() + 1
            val rows = (size.height / step).toInt() + 1

            // Creamos el ColorFilter que se aplicará a cada icono.
            val colorFilter = ColorFilter.tint(iconColor)

            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    val index = (row + col) % icons.size
                    val icon = icons[index]

                    translate(
                        left = col * step,
                        top = row * step
                    ) {
                        with(icon) {
                            // Aplicamos el ColorFilter al dibujar el icono.
                            draw(
                                size = Size(iconPx, iconPx),
                                colorFilter = colorFilter
                            )
                        }
                    }
                }
            }
        }
    }
}
