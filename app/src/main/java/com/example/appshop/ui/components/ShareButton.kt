package com.example.appshop.ui.components

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/**
 * Botón reutilizable para compartir contenido en redes o apps externas.
 *
 * @param title Título del contenido a compartir
 * @param description Descripción o cuerpo del mensaje
 * @param url (opcional) Enlace que se incluirá al final del mensaje
 * @param modifier Permite personalizar el layout desde donde se use
 */
@Composable
fun ShareButton(
    title: String,
    description: String,
    url: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = {
                val shareMessage = buildString {
                    append("🔥 $title 🔥\n")
                    append(description)
                    if (!url.isNullOrBlank()) append("\nConoce más en 👉 $url")
                }

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }

                context.startActivity(
                    Intent.createChooser(shareIntent, "Compartir promoción vía")
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Compartir contenido",
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Compartir",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
