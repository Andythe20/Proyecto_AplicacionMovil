package com.example.appshop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.appshop.R

val LatoFontFamily =
    FontFamily(
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
        Font(R.font.lato_black_italic, FontWeight.Black, FontStyle.Italic),
    )

val PacificoFontFamily =
    FontFamily(
        Font(R.font.pacifico_regular, FontWeight.Normal),
    )

// Set of Material typography styles to start with
val Typography =
    Typography(
        // Roles de Display: para texto muy grande y corto (ej. un "50%" en una pantalla de oferta)
        // Usamos Pacifico para un gran impacto visual.
        displayLarge =
            TextStyle(
                fontFamily = PacificoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 57.sp,
                lineHeight = 64.sp,
                letterSpacing = (-0.25).sp,
            ),
        displayMedium =
            TextStyle(
                fontFamily = PacificoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 45.sp,
                lineHeight = 52.sp,
                letterSpacing = 0.sp,
            ),
        // Roles de Headline: para títulos importantes en pantallas.
        // Usamos Lato en negrita para una buena legibilidad.
        headlineLarge =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
        headlineMedium =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
        // Roles de Title: para subtítulos y elementos de listas.
        titleLarge =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.SemiBold, // Un poco más de peso para destacar
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
        // Roles de Body: para el cuerpo de texto principal.
        bodyLarge =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
        // Roles de Label: para botones y texto pequeño.
        labelLarge =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
        labelMedium =
            TextStyle(
                fontFamily = LatoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
    )
