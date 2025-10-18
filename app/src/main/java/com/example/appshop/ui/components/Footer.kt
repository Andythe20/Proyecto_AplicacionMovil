package com.example.appshop.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appshop.R
import com.example.appshop.ui.theme.PacificoFontFamily

@Composable
fun FooterSection(openUrl: (String) -> Unit){
// --- SECCIÓN DEL NAVBAR ---
    Column(
        modifier = Modifier.fillMaxWidth(),
        // Esto centrará horizontalmente todo el contenido de la columna.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Un espacio superior para separar del contenido anterior.
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Síguenos en nuestras redes sociales!",
            fontFamily = PacificoFontFamily,
            fontSize = 20.sp,
            style = MaterialTheme.typography.titleMedium
        )

        // Un espacio entre el título y los iconos.
        Spacer(modifier = Modifier.height(16.dp))

        // Iconos
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                openUrl("https://www.instagram.com/onlyflans/")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_logo),
                    contentDescription = "Instagram",
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = {
                openUrl("https://www.facebook.com/onlyflans/")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_facebook),
                    contentDescription = "Facebook",
                    tint = Color.Unspecified
                )
            }
            IconButton(onClick = {
                openUrl("https://twitter.com/onlyflans")
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_twitter),
                    contentDescription = "Twitter",
                    tint = Color.Unspecified
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}