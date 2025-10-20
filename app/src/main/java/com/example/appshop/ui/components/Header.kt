package com.example.appshop.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appshop.R
import com.example.appshop.ui.theme.PacificoFontFamily

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun HeaderSection(userName: String) {

    val gradientColors = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.surface
    )

    // Define el ángulo del degradado (0° = izquierda a derecha, 90° = arriba a abajo)
    val gradientBrush = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(0f, Float.POSITIVE_INFINITY),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    // color para el borde inferior
    val borderColor = MaterialTheme.colorScheme.primary

    BoxWithConstraints {
        if (maxWidth < 600.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
                    .drawBehind {
                        // Dibuja el borde superior
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, 0f),
                            end = Offset(size.width, 0f),
                            strokeWidth = 8.dp.toPx()
                        )
                        // Dibuja el borde inferior
                        drawLine(
                            color = borderColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(15.dp),
                            clip = false
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.only_flans_logo),
                        contentDescription = "Logo OnlyFlans",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }
                Text(
                    text = "Hola, $userName!",
                    fontSize = 35.sp,
                    fontFamily = PacificoFontFamily,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 10.dp)
                )


            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(gradientBrush)
                    .border(4.dp, borderColor)
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(15.dp),
                            clip = false
                        )
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(15.dp)
                        )
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.only_flans_logo),
                        contentDescription = "Logo OnlyFlans",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }
                Text(
                    text = "Hola, $userName!",
                    fontSize = 35.sp,
                    fontFamily = PacificoFontFamily,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 10.dp)
                )


            }
        }

    }

}
