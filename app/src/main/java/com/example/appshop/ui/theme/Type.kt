package com.example.appshop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.appshop.R

val LatoFontFamily = FontFamily(
    // Estilos normales (no itálicos)
    Font(R.font.lato_thin, FontWeight.Thin),
    Font(R.font.lato_light, FontWeight.Light),
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_bold, FontWeight.Bold),
    Font(R.font.lato_black, FontWeight.Black),

    // Estilos itálicos
    Font(R.font.lato_thin_italic, FontWeight.Thin, FontStyle.Italic),
    Font(R.font.lato_light_italic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.lato_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.lato_bold_italic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.lato_black_italic, FontWeight.Black, FontStyle.Italic)
)

val PacificoFontFamily = FontFamily(
    Font(R.font.pacifico_regular, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography =
    Typography(
        // Estilo para el cuerpo de texto principal (párrafos, etc.)
        bodyLarge = TextStyle(
            fontFamily = LatoFontFamily, // Usa la fuente Lato
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        // Estilo para títulos grandes (ej. nombre de la pantalla)
        titleLarge = TextStyle(
            fontFamily = PacificoFontFamily, // Usa la fuente Pacifico para un toque decorativo
            fontWeight = FontWeight.Normal, // Pacifico solo tiene un peso, así que usamos Normal
            fontSize = 34.sp, // Un tamaño más grande para que luzca
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        // Estilo para títulos de secciones o elementos importantes
        headlineMedium = TextStyle(
            fontFamily = LatoFontFamily, // Usa Lato
            fontWeight = FontWeight.Bold, // En negrita para destacar
            fontSize = 24.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        // Estilo para etiquetas pequeñas o texto secundario
        labelSmall = TextStyle(
            fontFamily = LatoFontFamily, // Usa Lato
            fontWeight = FontWeight.Light, // Un peso ligero para que sea sutil
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
