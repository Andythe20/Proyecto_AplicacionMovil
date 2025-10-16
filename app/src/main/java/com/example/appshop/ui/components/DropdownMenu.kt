package com.example.appshop.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

/**
 * Un componente de menú desplegable personalizado y reutilizable.
 *
 * @param expanded Indica si el menú debe mostrarse.
 * @param onDismissRequest La función que se llama cuando el usuario pide cerrar el menú (ej. tocando fuera).
 * @param navController El controlador de navegación para manejar los clics.
 */
@Composable
fun AppDropdownMenu( // Renombrado para mayor claridad
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    navController: NavHostController
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest // Usamos la función que nos pasan
    ) {
        // Primera sección
        DropdownMenuItem(
            text = { Text("Mi Perfil") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            onClick = {
                // Aquí va la lógica de navegación
                onDismissRequest() // Cierra el menú
            }
        )
        DropdownMenuItem(
            text = { Text("Configuración") },
            leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
            onClick = {
                // Aquí va la lógica de navegación
                onDismissRequest() // Cierra el menú
            }
        )

        HorizontalDivider()

        // Segunda sección
        DropdownMenuItem(
            text = { Text("Contactar Soporte") },
            leadingIcon = { Icon(Icons.Outlined.Call, contentDescription = null) },
            trailingIcon = { Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null) },
            onClick = {
                // Aquí va la lógica de navegación
                onDismissRequest() // Cierra el menú
            }
        )

        HorizontalDivider()

        // Tercera sección: Cerrar Sesión
        DropdownMenuItem(
            text = { Text("Cerrar Sesión") },
            leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            onClick = {
                navController.navigate("auth") {
                    popUpTo(0) // Limpia todo el historial
                }
                onDismissRequest() // Cierra el menú
            }
        )
    }
}
